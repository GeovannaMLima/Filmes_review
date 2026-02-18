package my.Cinema.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import my.Cinema.config.JWTUserData;
import my.Cinema.models.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class TokenService {

    @Value("${JWT_SECRET}")
    private String secret;

    public String generateToken(UserModel user) {
         try{
             Algorithm algorithm = Algorithm.HMAC256(secret);
             return JWT.create()
                     .withIssuer("review-movies")
                     .withSubject(user.getLogin())
                     .withClaim("userId", user.getId())
                     .withExpiresAt(genExpirationDate())
                     .sign(algorithm);
         }catch (JWTCreationException exception){
             return "";
         }
    }

    public Optional<JWTUserData> validateToken(String token) {
        try{
            DecodedJWT decode= JWT.require(Algorithm.HMAC256(secret)).build().verify(token);

            return Optional.of(JWTUserData.builder()
                    .userId(decode.getClaim("userId").asLong())
                    .email(decode.getSubject())
                    .role(decode.getClaim("role").asString())
                    .build());
        }
        catch(JWTVerificationException e){
            return Optional.empty();
        }
    }



    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
