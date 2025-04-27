package com.sprintpredictor.security.jwt;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtils {

    @Value("${sprintpredictor.app.jwtSecret}")
    private String jwtSecret;

    @Value("${sprintpredictor.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // 🚫 REMOVE this constructor:
    // public JwtUtils(String jwtSecret, int jwtExpirationMs) { ... }

    private Key getSigningKey() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(jwtSecret.getBytes());
            return new SecretKeySpec(hash, "HmacSHA256");
        } catch (Exception e) {
            throw new RuntimeException("Error generating signing key", e);
        }
    }

    public String generateJwtToken(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTime = currentTimeMillis + jwtExpirationMs;
        String payload = String.format("{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}", username, currentTimeMillis, expirationTime);

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(getSigningKey());
            byte[] signature = mac.doFinal(payload.getBytes());
            String encodedPayload = Base64.getEncoder().encodeToString(payload.getBytes());
            String encodedSignature = Base64.getEncoder().encodeToString(signature);
            return encodedPayload + "." + encodedSignature;
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public String getUserNameFromJwtToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
        String payload = new String(Base64.getDecoder().decode(parts[0]));
        String[] payloadParts = payload.split(",");
        for (String part : payloadParts) {
            if (part.contains("\"sub\":\"")) {
                return part.split(":")[1].replace("\"", "");
            }
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            String[] parts = authToken.split("\\.");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }

            String payload = new String(Base64.getDecoder().decode(parts[0]));
            String expectedSignature = parts[1];

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(getSigningKey());
            byte[] signature = mac.doFinal(payload.getBytes());
            String actualSignature = Base64.getEncoder().encodeToString(signature);

            return expectedSignature.equals(actualSignature);
        } catch (Exception e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }
}
