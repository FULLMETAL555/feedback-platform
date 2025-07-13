package com.feedback.feedback_service.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtil {

    public static String hashApiKey(String apiKey){
        try {
            MessageDigest digest =MessageDigest.getInstance("SHA-256");
            byte[] hashed =digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing API key",e);
        }
    }
}
