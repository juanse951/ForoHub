package com.aluracursos.Foro.Hub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public String generarToken(){
        try {
            Algorithm algorithm = Algorithm.HMAC256("8914015051Juan@");
            return JWT.create()
                    .withIssuer("Foro Hub")
                    .withSubject("juanse951@gmail.com")
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("error al generar el  token jwt", exception);
        }
    }
}
