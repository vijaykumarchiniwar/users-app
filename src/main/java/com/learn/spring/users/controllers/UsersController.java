package com.learn.spring.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.spring.users.models.UserDto;
import com.learn.spring.users.models.UserRequestModel;
import com.learn.spring.users.models.UserResponseModel;
import com.learn.spring.users.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "/status/check")
    private String getStatus() {
        return "Running...";
    }

    @GetMapping(value = "/{email}", produces = {APPLICATION_JSON_VALUE})
    private ResponseEntity<UserResponseModel> getUser(@PathVariable String email) {
        UserDto userDto = userServiceImpl.getUser(email);
        UserResponseModel userResponseModel = objectMapper.convertValue(userDto, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.FOUND).body(userResponseModel);
    }

    @GetMapping()
    private ResponseEntity<List<UserResponseModel>> getUsers(@RequestParam(value = "page", defaultValue = "1")
                                                                     int page, @RequestParam(value = "limit", defaultValue = "25") int limit) {

        if (page < 0) {
            page = 0;
        }
        if (page > 0) {
            page = --page;
        }
        List<UserDto> userDtos = userServiceImpl.getUsers(page, limit);

        List<UserResponseModel> userResponseModels = new ArrayList<>(userDtos.size());
        userDtos.forEach(userDto -> {
            UserResponseModel userResponseModel = objectMapper.convertValue(userDto, UserResponseModel.class);
            userResponseModels.add(userResponseModel);
        });
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponseModels);
    }

    @PostMapping
    private ResponseEntity<UserResponseModel> createUser(@RequestBody UserRequestModel userRequestModel) {
        UserDto userDto = objectMapper.convertValue(userRequestModel, UserDto.class);
        userDto = userServiceImpl.createUser(userDto);
        UserResponseModel userResponseModel = objectMapper.convertValue(userDto, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseModel);
    }

    @PutMapping
    private ResponseEntity<UserResponseModel> updateUser(@RequestBody UserRequestModel userRequestModel) {
        UserDto userDto = objectMapper.convertValue(userRequestModel, UserDto.class);
        userDto = userServiceImpl.updateUser(userDto);
        if (userDto != null)
            throw new NullPointerException();
        UserResponseModel userResponseModel = objectMapper.convertValue(userDto, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.FOUND).body(userResponseModel);
    }

    @DeleteMapping(value = "/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    private ResponseEntity<UserResponseModel> deleteUser(@PathVariable String email) {
        UserDto userDto = userServiceImpl.deleteUser(email);
        UserResponseModel userResponseModel = objectMapper.convertValue(userDto, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponseModel);
    }

}
