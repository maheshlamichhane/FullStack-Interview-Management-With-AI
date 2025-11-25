package com.itsutra.project.mapper;

import com.itsutra.project.dto.SignUpRequest;
import com.itsutra.project.entity.User;
import com.itsutra.project.utilities.PasswordUtil;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationMapper {

    private PasswordUtil passwordUtil;

    public User toEntity(SignUpRequest request) {
        User entity = new User();
        entity.setEmail(request.getEmail());
        String salt = passwordUtil.generateSalt();
        entity.setSalt(salt);
        String hashPassword = passwordUtil.hashPassword(request.getPassword(), salt);
        entity.setPasswordHash(hashPassword);
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setIsActive(true);
        entity.setEmailVerified(false);
        return entity;
    }

}
