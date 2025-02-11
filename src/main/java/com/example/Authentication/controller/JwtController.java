package com.example.Authentication.controller;

import com.example.Authentication.dto.req.CreateJwtRequest;
import com.example.Authentication.dto.res.CreateJwtResponse;
import com.example.Authentication.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;

    @PostMapping()
    ResponseEntity<?> createJwt(@RequestBody CreateJwtRequest createJwtRequest) {
        try {
            return ResponseEntity.ok(jwtService.createJwt(createJwtRequest));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

}
