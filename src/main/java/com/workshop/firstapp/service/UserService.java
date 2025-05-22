package com.workshop.firstapp.service;

import com.workshop.firstapp.exception.BadRequestException;
import com.workshop.firstapp.exception.ResourceNotFoundException;
import com.workshop.firstapp.mapper.UserMapper;
import com.workshop.firstapp.model.User;
import com.workshop.firstapp.model.UserRequest;
import com.workshop.firstapp.model.UserResponse;
import com.workshop.firstapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    public UserService(UserRepository repository, UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    public UserResponse createUser(UserRequest request) {
        // Check if email already exists
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists: " + request.getEmail());
        }

        User user = userMapper.userRequestToUser(request);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = repository.save(user);
        return userMapper.userToUserResponse(saved);
    }

    public List<UserResponse> getAllUsers() {
        return userMapper.usersToUserResponses(repository.findAll());
    }

    public UserResponse getUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
        return userMapper.userToUserResponse(user);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setPhone(request.getPhone());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = repository.save(user);
        return userMapper.userToUserResponse(saved);
    }

    public void deleteUser(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User", id.toString());
        }
        repository.deleteById(id);
    }
}