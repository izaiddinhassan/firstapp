package com.workshop.firstapp.service;

import com.workshop.firstapp.model.Greeting;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class GreetingService {

    public Greeting getGreeting(){
        String msg = "Hello from Greeting Service";
        int version = Year.now().getValue();
        return new Greeting(msg, version);
    }
}
