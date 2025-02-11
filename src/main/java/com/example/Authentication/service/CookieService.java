package com.example.Authentication.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CookieService {

    public void createAuthCookie(HttpServletResponse response, String token){
        Cookie cookie = new Cookie("auth", token);
        cookie.setPath("/");
        cookie.setMaxAge(900);
        response.addCookie(cookie);
    }

    public Cookie getAuthCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(Objects.equals(cookie.getName(), "auth")){
                return cookie;
            }
        }
        return null;
    }
}
