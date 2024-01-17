package com.worktimetrace.usermanagement.Repository;

import org.springframework.data.repository.CrudRepository;

import com.worktimetrace.usermanagement.Entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>{
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
}