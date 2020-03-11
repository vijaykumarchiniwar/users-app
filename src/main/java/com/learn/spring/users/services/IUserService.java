package com.learn.spring.users.services;

import com.learn.spring.users.models.UserDto;

import java.util.List;

public interface IUserService {
    public UserDto createUser(UserDto userDto);

    public UserDto deleteUser(String email);

    public UserDto updateUser(UserDto userDto);

    public UserDto getUser(String email);

    List<UserDto> getUsers(int page, int limit);
}
