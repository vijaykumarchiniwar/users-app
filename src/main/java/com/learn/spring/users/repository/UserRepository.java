package com.learn.spring.users.repository;

import com.learn.spring.users.entities.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    public UserEntity findUserEntityByEmail(String email);
}
