package com.example.Authentication.service;

import com.example.Authentication.dto.req.CreateJwtRequest;
import com.example.Authentication.dto.req.LoginRequest;
import com.example.Authentication.dto.req.SignupRequest;
import com.example.Authentication.dto.res.CreateJwtResponse;
import com.example.Authentication.dto.res.SignupResponse;
import com.example.Authentication.dto.res.UserDetailResponse;
import com.example.Authentication.entities.User;
import com.example.Authentication.repositories.AuthenticationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationRepo authenticationRepo;
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public SignupResponse signup(SignupRequest signupReq){
        try {
            if(authenticationRepo.existsByUsername(signupReq.getUsername())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "user name is already taken");
            }
            User user  = User.builder()
                    .username(signupReq.getUsername())
                    .password(signupReq.getPassword())
                    .build();

            User savedUser = authenticationRepo.save(user);
            return new SignupResponse(
                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getPassword()
            );
        }
        catch (ResponseStatusException e) {
            throw e;
        }
    }

    public String login(LoginRequest loginRequest){
        try {
            User user = authenticationRepo.findByUsername(loginRequest.getUsername());

            if(user == null){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "no user find for this user name");
            }

            if(!Objects.equals(user.getPassword(), loginRequest.getPassword())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "password did not match");
            }

            CreateJwtRequest createJwtRequest = new CreateJwtRequest(
                    user.getId(),
                    jwtSecret
            );
            CreateJwtResponse createJwtResponse = jwtService.createJwt(createJwtRequest);
            return createJwtResponse.getToken();
        }
        catch(ResponseStatusException e){
            throw e;
        }
    }

    // get payload service
    public Long getPayload(String token){
        String payload = jwtService.getJwtPayload(jwtSecret, token);
        return Long.parseLong(payload);
    }

    public UserDetailResponse getUserDetail(Long id){
        try {
            User user = authenticationRepo.findById(id).orElse(null);
            if(user == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user is not exist for this id");
            }

            return new UserDetailResponse(user.getUsername());
        } catch (ResponseStatusException e) {
            throw e;
        }
    }

}
