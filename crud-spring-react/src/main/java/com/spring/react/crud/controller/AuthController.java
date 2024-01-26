package com.spring.react.crud.controller;

import com.spring.react.crud.dto.LoginRequest;
import com.spring.react.crud.dto.TokenResponse;
import com.spring.react.crud.dto.WebResponse;
import com.spring.react.crud.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginRequest request){
        TokenResponse login = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(login).build();
    }


}
