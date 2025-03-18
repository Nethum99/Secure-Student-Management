package com.nethum.springsecuirtydemo.services;

import com.nethum.springsecuirtydemo.model.User;
import com.nethum.springsecuirtydemo.repo.UserRepo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

    @Autowired
    private UserRepo userRepo;

    private static final String SECRET = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg=\r\n";

    private String secretKey;

    public JwtService(){
        secretKey = generateSecretKey();
    }

    private String generateSecretKey(){     //to generate token we need secret key and we can generate secret key like this and we can hardcode like above
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGenerator.generateKey();
            System.out.println("Secret Key : "+secretKey.toString());
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }
        catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Error genrating secret key",e);
        }
    }

    public String generateAccessToken(String userName) {      //generate token

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*1))  //expirations is 3 minutes
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generate Refresh Token (long-lived)
    public String generateRefreshToken(String userName) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // Refresh token expires in 24 hours
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    private Key getKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUserName(String token) {
        //Extract username from jwt tocken
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();

    }

    public boolean validateToken(String token) {
        try {
            final String userName = extractUserName(token);  // Extract username from token

            // Check token expiration and the username
            if (userName != null && !isTokenExpired(token)) {
                User user = userRepo.findByUserName(userName);  // Ensure the user exists in DB
                if (user != null) {
                    // Verify the token's signature
                    Jwts.parserBuilder()
                            .setSigningKey(getKey())  // Use the same key used for signing the token
                            .build()
                            .parseClaimsJws(token);  // This will throw exceptions if signature is invalid
                    return true;
                }
            }
        } catch (SignatureException e) {
            System.out.println("JWT signature does not match. Token is invalid.");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired.");
        } catch (MalformedJwtException e) {
            System.out.println("JWT token is malformed.");
        } catch (Exception e) {
            System.out.println("Unexpected error during JWT validation.");
        }
        return false; // Return false if any error occurs
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

}

