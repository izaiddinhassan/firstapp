package com.workshop.firstapp.controller;

import com.workshop.firstapp.model.Greeting;
import com.workshop.firstapp.service.GreetingService;
import com.workshop.firstapp.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final GreetingService greetingService;
    private final MessageService messageService;

    public HelloController(GreetingService greetingService, MessageService messageService) {
        this.greetingService = greetingService;
        this.messageService = messageService;
    }

    @GetMapping("/hello")
    public Greeting hello(){
        return greetingService.getGreeting();
    }

    @GetMapping("/messages")
    public List<String> messages(){
        return messageService.getProcessedMessages();
    }


}
