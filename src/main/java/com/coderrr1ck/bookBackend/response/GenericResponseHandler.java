package com.coderrr1ck.bookBackend.response;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GenericResponseHandler {
    private final Map<String,String> response ;

    public GenericResponseHandler(){
        this.response = new HashMap<>();
    }

    public ResponseEntity<Map<String,String>> setResponse(String message){
        this.response.put("message",message);
        return ResponseEntity.ok(response);
    }
}
