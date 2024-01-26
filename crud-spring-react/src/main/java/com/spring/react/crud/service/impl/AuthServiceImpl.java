package com.spring.react.crud.service.impl;

import com.spring.react.crud.dto.LoginRequest;
import com.spring.react.crud.dto.TokenResponse;
import com.spring.react.crud.model.User;
import com.spring.react.crud.repository.UserRepository;
import com.spring.react.crud.service.AuthService;
import com.spring.react.crud.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;


    @Transactional
    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        validationService.validate(loginRequest);

        User user = userRepository.findById(loginRequest.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if(loginRequest.getPassword().equals(user.getPassword())){
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next10Minutes());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .tokenExpiredAt(user.getTokenExpiredAt())
                    .build();
        }else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }
    }

    private Long next10Minutes() {
        return System.currentTimeMillis() + (1000 * 60 * 10);
    }
}
