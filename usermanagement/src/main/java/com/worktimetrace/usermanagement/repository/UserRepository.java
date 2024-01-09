package com.worktimetrace.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktimetrace.usermanagement.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}

