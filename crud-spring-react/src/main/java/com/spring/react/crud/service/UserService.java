package com.spring.react.crud.service;

import com.spring.react.crud.dto.RegisterUserRequest;
import com.spring.react.crud.dto.UpdateUserRequest;
import com.spring.react.crud.dto.UserResponse;

import java.util.List;

public interface UserService {
    public void register(RegisterUserRequest request);

    public UserResponse getUserById(String userId);

    public List<UserResponse> getAllUser();

    public UserResponse updateUserById(String userId, UpdateUserRequest request);

    public void deleteUserById(String userId);
}
