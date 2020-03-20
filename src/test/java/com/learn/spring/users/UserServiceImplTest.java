package com.learn.spring.users;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.spring.users.entities.AddressEntity;
import com.learn.spring.users.entities.UserEntity;
import com.learn.spring.users.exceptions.UserException;
import com.learn.spring.users.models.UserDto;
import com.learn.spring.users.repository.UserRepository;
import com.learn.spring.users.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    UserEntity userEntity;
    UserDto userDto;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(34534534);
        userEntity.setEmail("vijaykumar.chiniwar100@gmail.com");
        userEntity.setUserId("asdfasdf34345sdfs");
        userEntity.setEncryptedPassword("asdfadf453");
        userEntity.setFirstName("Vijay");
        userEntity.setLastName("Chiniwar");

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUserDetails(userEntity);
        addressEntity.setAddressId("asdfasdf");
        addressEntity.setCity("Bangalore");
        addressEntity.setPinCode("560076");
        addressEntity.setType("Home");
        addressEntity.setStreet("7th A main");
        List<AddressEntity> addressEntities = new ArrayList<AddressEntity>();
        addressEntities.add(addressEntity);

        userEntity.setAddresses(addressEntities);

        ObjectMapper objectMapperLocal = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        userDto = objectMapperLocal.convertValue(userEntity, UserDto.class);

        //Setting password explicitly
        userDto.setPassword(userEntity.getEncryptedPassword());

    }

    @Test()
    public void testGetUser() {
        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(userEntity);
        when(objectMapper.convertValue(userEntity, UserDto.class)).thenReturn(userDto);

        UserDto userDto = userService.getUser("vijaykumar.chiniwar100@gmail.com");

        assertEquals("vijaykumar.chiniwar100@gmail.com", userDto.getEmail());
        assertEquals(1, userDto.getAddressDtos().size());
        assertEquals("560076", userDto.getAddressDtos().get(0).getPinCode());
        assertNotNull(userDto, "ÜserDto is null");
    }

    @Test()
    public void testGetUserThrowsException() {
        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(null);
        assertThrows(UserException.class, () -> {
            userService.getUser("test@test.com");
        });
    }
}
