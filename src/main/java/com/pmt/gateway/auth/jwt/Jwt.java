package com.pmt.gateway.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class Jwt {

    private JWTVerifier jwtVerifier;

    private JWTParser jwtParser;

    public Jwt() {
        final Algorithm algorithm = Algorithm.HMAC256("secret");
        this.jwtVerifier = JWT.require(algorithm).build();
        this.jwtParser = new JWTParser();
    }

    public boolean isValid(String token) {
        try {
            jwtVerifier.verify(token);
            return true;
        } catch(JWTVerificationException e) {
            return false;
        }
    }

    public DecodedToken decode(String token) {
        final DecodedJWT decodedJWT = JWT.decode(token);
        final Payload payload = jwtParser.parsePayload(decodedJWT.getPayload());
        final Map<String, Claim> claims = payload.getClaims();
        if(!claims.containsKey("role")) throw new RuntimeException("Auth token does not contain a role");
        return new DecodedToken(decodedJWT.getSubject(), claims.get("role").asString());
    }

}
