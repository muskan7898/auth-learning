package com.example.Authentication.controller;

import com.example.Authentication.dto.req.LoginRequest;
import com.example.Authentication.dto.req.SignupRequest;
import com.example.Authentication.service.AuthenticationService;
import com.example.Authentication.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequestMapping("/authentication")
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final CookieService cookieService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupReq){
        try {
            return ResponseEntity.ok(authenticationService.signup(signupReq));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        try {
            String token = authenticationService.login(loginRequest);
            cookieService.createAuthCookie(response, token);
            return ResponseEntity.ok("login success");
        } catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetail(HttpServletRequest request){
        try {
            Cookie cookie = cookieService.getAuthCookie(request);
            if(cookie == null){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "please login");
            }
            String token = cookie.getValue();
            Long id = authenticationService.getPayload(token);
             return ResponseEntity.ok(authenticationService.getUserDetail(id));

        }
        catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
        }
        catch(Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
