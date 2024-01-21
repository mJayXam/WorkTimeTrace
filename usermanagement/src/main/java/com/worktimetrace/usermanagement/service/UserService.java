package com.worktimetrace.usermanagement.service;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.worktimetrace.usermanagement.DTO.UserInfoDTO;
import com.worktimetrace.usermanagement.Entity.UserEntity;
import com.worktimetrace.usermanagement.Repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    public UserInfoDTO getUserInfoByUsername(String username) {
        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            return null;
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setFirstname(user.getFirstname());
        userInfoDTO.setLastname(user.getLastname());
        userInfoDTO.setUsername(user.getUsername());
        userInfoDTO.setStreet(user.getStreet());
        userInfoDTO.setHousenumber(user.getHousenumber());
        userInfoDTO.setZipcode(user.getZipcode());
        userInfoDTO.setCity(user.getCity());

        return userInfoDTO;
    }
}