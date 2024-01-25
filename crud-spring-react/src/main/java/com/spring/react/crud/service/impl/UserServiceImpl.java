package com.spring.react.crud.service.impl;

import com.spring.react.crud.dto.RegisterUserRequest;
import com.spring.react.crud.model.User;
import com.spring.react.crud.repository.UserRepository;
import com.spring.react.crud.service.UserService;
import com.spring.react.crud.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
}
