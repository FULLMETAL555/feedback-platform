package com.feedback.feedback_service.security;


import org.apache.coyote.Request;
import org.springframework.stereotype.Service;

import java.net.FileNameMap;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<String, RequestInfo> requestMap =new ConcurrentHashMap<>();

    private final int MAX_REQUEST=100;
    private final long WINDOW_DURATION_MS = 60_000;

    public boolean isAllowed(String apiKey){
        long currentTime = Instant.now().toEpochMilli();

        RequestInfo info=requestMap.getOrDefault(apiKey,new RequestInfo(0,currentTime));

        if(currentTime-info.startTime>WINDOW_DURATION_MS){
            info = new RequestInfo(1,currentTime);

        } else {
            if (info.requestCount>=MAX_REQUEST){
                return false;
            }
            info.requestCount++;
        }
        requestMap.put(apiKey,info);
        return true;

    }

    private static class RequestInfo {
        int requestCount;
        long startTime;

        public RequestInfo(int count, long startTime) {
            this.requestCount = count;
            this.startTime = startTime;
        }
    }
}
