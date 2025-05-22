package com.workshop.firstapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String status;
    private Integer age;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}