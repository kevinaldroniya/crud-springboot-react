package com.spring.react.crud.service;

import com.spring.react.crud.dto.LoginRequest;
import com.spring.react.crud.dto.TokenResponse;

public interface AuthService {

    public TokenResponse login(LoginRequest loginRequest);
}
