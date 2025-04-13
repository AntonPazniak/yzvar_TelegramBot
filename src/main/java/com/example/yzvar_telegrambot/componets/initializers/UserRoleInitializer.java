package com.example.yzvar_telegrambot.componets.initializers;

import com.example.yzvar_telegrambot.entities.user.UserRole;
import com.example.yzvar_telegrambot.enums.UserRoleEnum;
import com.example.yzvar_telegrambot.repositories.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("userRoleInitializer")
@RequiredArgsConstructor
public class UserRoleInitializer {

    private final UserRoleRepository userRoleRepository;


    @PostConstruct
    public void initRoles() {
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            userRoleRepository.findByName(roleEnum).orElseGet(() -> {
                UserRole role = UserRole.builder().name(roleEnum).build();
                return userRoleRepository.save(role);
            });
        }
    }
}
