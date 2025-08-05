package com.feedback.feedback_service.security;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklistService {


    private final Map<String,Long> blacklist =new ConcurrentHashMap<>();

    public void blackListToken(String token,long expiryTimeMillis){
        blacklist.put(token,expiryTimeMillis);
    }

    public boolean isBlacklisted(String token){
        Long expiry= blacklist.get(token);

        if(expiry==null) return false;

        if(expiry<System.currentTimeMillis()){
            blacklist.remove(token);
            return false;
        }
        return true;
    }
}
