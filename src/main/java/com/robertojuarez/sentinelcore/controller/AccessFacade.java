package com.robertojuarez.sentinelcore.controller;

import com.robertojuarez.sentinelcore.model.AccessLog;
import com.robertojuarez.sentinelcore.model.User;
import com.robertojuarez.sentinelcore.repository.AccessLogRepository;
import com.robertojuarez.sentinelcore.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AccessFacade {

    private final UserService userService;
    private final AccessLogRepository accessLogRepository;

    public AccessFacade(UserService userService, AccessLogRepository accessLogRepository)
    {
        this.userService = userService;
        this.accessLogRepository = accessLogRepository;
    }

    public String evaluatedAccess(String rfidUid)
    {
        Optional<User> optionalUser = userService.findByRfid(rfidUid);
        boolean granted = optionalUser.isPresent();
        String message;

        if(granted)
        {
            User user = optionalUser.get();

            message =  "¡Acceso concedido! Bienvenido " + user.getName();
        }
        else {
            message = "Acceso denegado: Tarjeta no reconocida.";
        }

        AccessLog accessLog = new AccessLog(
                rfidUid,
                LocalDateTime.now(),
                granted,
                message
        );

        accessLogRepository.save(accessLog);

        return message;

    }
}
