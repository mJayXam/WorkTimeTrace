package com.worktimetrace.usermanagement.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.worktimetrace.usermanagement.Entity.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
Optional<RoleEntity> findByName(String name);
}