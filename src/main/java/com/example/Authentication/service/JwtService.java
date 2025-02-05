package com.example.Authentication.service;

import com.example.Authentication.dto.req.CreateJwtRequest;
import com.example.Authentication.dto.res.CreateJwtResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final ObjectMapper objectMapper;

    public CreateJwtResponse createJwt(CreateJwtRequest createJwtRequest){
        try {
            // convert java object to json
            String jsonPayload = objectMapper.writeValueAsString(createJwtRequest.getPayload());

            //Jwt Payload (claim set)
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .claim("data", jsonPayload)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hr expiry
                    .build();

            //creating the Header
            SignedJWT signedJWT  = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            //signing
            signedJWT.sign(new MACSigner(createJwtRequest.getSecret().getBytes()));
            return new CreateJwtResponse(signedJWT.serialize());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create jwt");
        }
    }

    // get payload from token
    public String getJwtPayload(String secret, String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(secret.getBytes());
            if(signedJWT.verify(verifier)){
                return signedJWT.getJWTClaimsSet().getStringClaim("data");
            }
            else {
                throw new RuntimeException("Invalid JWT signature");
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT", e);
        }
    }


}
