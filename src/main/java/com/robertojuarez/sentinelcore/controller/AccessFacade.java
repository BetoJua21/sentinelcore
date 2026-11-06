package com.robertojuarez.sentinelcore.controller;

import com.robertojuarez.sentinelcore.model.AccessLog;
import com.robertojuarez.sentinelcore.model.User;
import com.robertojuarez.sentinelcore.repository.AccessLogRepository;
import com.robertojuarez.sentinelcore.service.UserService;
import org.springframework.stereotype.Component;
import com.robertojuarez.sentinelcore.dto.AccessResponse;

import java.time.Instant;
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

    public AccessResponse evaluatedAccess(String rfidUid)
    {
        Optional<User> optionalUser = userService.findByRfid(rfidUid);
        boolean granted = optionalUser.map(User::isActive).orElse(false);
        String message;

        if (optionalUser.isEmpty()) {
            message = "Acceso denegado: Tarjeta no reconocida.";
        } else if (!optionalUser.get().isActive()) {
            message = "Acceso denegado: Usuario inactivo.";
        } else {
            User user = optionalUser.get();
            message = "¡Acceso concedido! Bienvenido " + user.getName();
        }

        Instant timestamp = Instant.now();

        AccessLog accessLog = new AccessLog(
                rfidUid,
                timestamp,
                granted,
                message
        );

        accessLogRepository.save(accessLog);

        return new AccessResponse(granted,message);

    }
}
