package com.spring.react.crud.controller;

import com.spring.react.crud.dto.RegisterUserRequest;
import com.spring.react.crud.dto.UpdateUserRequest;
import com.spring.react.crud.dto.UserResponse;
import com.spring.react.crud.dto.WebResponse;
import com.spring.react.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterUserRequest request){
        userService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/users/{userId}",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> getUser(@PathVariable("userId")String userId){
        UserResponse userById = userService.getUserById(userId);
        return WebResponse.<UserResponse>builder().data(userById).build();
    }

    @GetMapping(
            path = "/api/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<UserResponse>> getAllUser(){
        List<UserResponse> userResponseList = userService.getAllUser();
        return WebResponse.<List<UserResponse>>builder().data(userResponseList).build();
    }

    @PatchMapping(
            path = "/api/users/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> updateUser(
            @PathVariable("userId")String userId,
            @RequestBody UpdateUserRequest request
            ){
        UserResponse userResponse = userService.updateUserById(userId, request);

        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @DeleteMapping(
            path = "/api/users/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteUser(@PathVariable("userId")String userId){
        userService.deleteUserById(userId);
        return WebResponse.<String>builder().data("User Deleted Successfully").build();
    }
}
