package com.learn.spring.users.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.spring.users.entities.UserEntity;
import com.learn.spring.users.exceptions.UserException;
import com.learn.spring.users.models.AddressDto;
import com.learn.spring.users.models.UserDto;
import com.learn.spring.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    // @Autowired
    private ObjectMapper objectMapper;

    // @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntityFromDb = userRepository.findUserEntityByEmail(userDto.getEmail());
        if (userEntityFromDb != null) {
            throw new UserException("User already exists.");
        }
        userDto.getAddressDtos().forEach(addressDto -> {
            addressDto.setAddressId(UUID.randomUUID().toString());
        });
        userDto.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = objectMapper.convertValue(userDto, UserEntity.class);
        userEntity.setEncryptedPassword(userDto.getPassword());
        userRepository.save(userEntity);

        UserDto savedUserDto = objectMapper.convertValue(userEntity, UserDto.class);
        return savedUserDto;
    }

    @Override
    public UserDto deleteUser(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email);
        if (userEntity == null) {
            throw new UserException("User not found exception.");
        }
        userRepository.delete(userEntity);
        UserDto userDto = objectMapper.convertValue(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(userDto.getEmail());
        if (userEntity == null) {
            throw new UserException("User not found exception.");
        }
        UserEntity userEntityCopied = objectMapper.convertValue(userDto, UserEntity.class);
        userEntityCopied.setEncryptedPassword(userEntity.getEncryptedPassword());
        userEntityCopied.setId(userEntity.getId());
        userEntity = userRepository.save(userEntityCopied);
        userDto = objectMapper.convertValue(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email);
        if (userEntity == null) {
            throw new UserException("User not found exception.");
        }
        // UserDto userDto = new UserDto();
        // BeanUtils.copyProperties(userEntity, userDto);
        UserDto userDto = objectMapper.convertValue(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<UserEntity> entityPage = userRepository.findAll(pageable);
        List<UserEntity> userEntities = entityPage.getContent();
        List<UserDto> userDtos = new ArrayList<>(userEntities.size());
        userEntities.forEach(userEntity -> {
            UserDto userDto = objectMapper.convertValue(userEntity, UserDto.class);
            userEntity.getAddresses().forEach(addressEntity -> {
                AddressDto addressDto = objectMapper.convertValue(addressEntity, AddressDto.class);
                userDto.getAddressDtos().add(addressDto);
            });
            userDtos.add(userDto);
        });
        return userDtos;
    }

}
