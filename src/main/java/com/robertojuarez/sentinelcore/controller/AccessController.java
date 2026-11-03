package com.robertojuarez.sentinelcore.controller;

import com.robertojuarez.sentinelcore.controller.AccessFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access")
public class AccessController {

    private final AccessFacade accessFacade;
    public AccessController(AccessFacade accessFacade)
    {
        this.accessFacade = accessFacade;
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyAccess(@RequestParam String rfid)
    {
        String result = accessFacade.evaluatedAccess(rfid);

        return ResponseEntity.ok(result);
    }

}
