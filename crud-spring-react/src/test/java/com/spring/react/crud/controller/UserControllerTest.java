package com.spring.react.crud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.react.crud.dto.RegisterUserRequest;
import com.spring.react.crud.dto.UpdateUserRequest;
import com.spring.react.crud.dto.UserResponse;
import com.spring.react.crud.dto.WebResponse;
import com.spring.react.crud.model.User;
import com.spring.react.crud.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void testRegisteruserSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("unit-test-1");
        request.setUsername("unit-test-1");
        request.setPassword("unit-test-1");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertEquals("OK", response.getData());
                }
        );
    }


    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setName("unit-test-1");
        user.setUsername("unit-test-1");
        user.setPassword("unit-test-1");
        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("unit-test-1");
        request.setUsername("unit-test-1");
        request.setPassword("unit-test-1");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        user.setName("unit-test-1");
        user.setPassword("unit-test-1");
        user.setUsername("unit-test-1");
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/unit-test-1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNull(response.getErrors());

                    assertEquals(user.getName(), response.getData().getName());
                    assertEquals(user.getUsername(), response.getData().getUsername());
                }
        );
    }

    @Test
    void testGetAllUser() throws Exception {
//        User user1 = new User();
//        user1.setName("unit-test-1");
//        user1.setPassword("unit-test-1");
//        user1.setUsername("unit-test-1");
//        userRepository.save(user1);
//
//        User user2 = new User();
//        user2.setName("unit-test-2");
//        user2.setPassword("unit-test-2");
//        user2.setUsername("unit-test-2");
//        userRepository.save(user2);

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername("unit-test-"+i);
            user.setName("unit-test-"+i);
            userRepository.save(user);
        }

        mockMvc.perform(
                get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<UserResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNull(response.getErrors());
                    assertEquals(5, response.getData().size());
                }
        );
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("unit-test-1");
        user.setPassword("unit-test-1");
        user.setName("unit-test-1");
        userRepository.save(user);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("updated-name");
        request.setPassword("updated-password");

        mockMvc.perform(
                patch("/api/users/unit-test-1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNull(response.getErrors());
                    assertEquals("updated-name",response.getData().getName());

                    User userDB = userRepository.findById(response.getData().getUsername()).orElse(null);
                    assertNotNull(userDB);

                    //using bcrypt
                    //assertTrue();

                }
        );
    }


    @Test
    void deleteUser() throws Exception {
        User user = new User();
        user.setUsername("unit-test-1");
        user.setName("unit-test-1");
        user.setPassword("unit-test-1");
        userRepository.save(user);

        mockMvc.perform(
                delete("/api/users/unit-test-1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertNull(response.getErrors());
                    assertEquals("User Deleted Successfully", response.getData());
                    assertFalse(userRepository.existsById("unit-test-1"));
                }
        );
    }

}