package com.workshop.firstapp.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    public List<String> getProcessedMessages(){
        List<String> messages = Arrays.asList("Spring","Boot","Workshop");
        return messages.stream()
                .map(String::toUpperCase)
                .filter(m -> m.length() > 4)
                .collect(Collectors.toList());
    }
}
