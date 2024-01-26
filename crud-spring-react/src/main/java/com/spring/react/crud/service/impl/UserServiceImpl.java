package com.spring.react.crud.service.impl;

import com.spring.react.crud.dto.RegisterUserRequest;
import com.spring.react.crud.dto.UpdateUserRequest;
import com.spring.react.crud.dto.UserResponse;
import com.spring.react.crud.model.User;
import com.spring.react.crud.repository.UserRepository;
import com.spring.react.crud.service.UserService;
import com.spring.react.crud.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;


    @Transactional
    @Override
    public void register(RegisterUserRequest request) {
        validationService.validate(request);
        if (userRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username Already Registered !");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getName());

        userRepository.save(user);
    }

    private UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Override
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        return toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toUserResponse).toList();
    }

    @Override
    public UserResponse updateUserById(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        validationService.validate(request);

        if (Objects.nonNull(request.getName())){
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())){
            user.setPassword(request.getPassword());
        }

        userRepository.save(user);

        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }

    @Override
    public void deleteUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        userRepository.deleteById(userId);
    }
}
