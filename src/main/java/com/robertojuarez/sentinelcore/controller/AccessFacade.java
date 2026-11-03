package com.robertojuarez.sentinelcore.controller;

import com.robertojuarez.sentinelcore.model.User;
import com.robertojuarez.sentinelcore.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccessFacade {

    private final UserService userService;

    public AccessFacade(UserService userService)
    {
        this.userService = userService;
    }

    public String evaluatedAccess(String rfidUid)
    {
        Optional<User> optionalUser = userService.findByRfid(rfidUid);

        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();

            return "¡Acceso concedido! Bienvenido " + user.getName();
        }
        else {
            return "Acceso denegado: Tarjeta no reconocida.";
        }
    }
}
