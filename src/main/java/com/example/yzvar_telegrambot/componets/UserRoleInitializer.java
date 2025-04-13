package com.example.yzvar_telegrambot.componets;

import com.example.yzvar_telegrambot.entities.UserRole;
import com.example.yzvar_telegrambot.enums.UserRoleEnum;
import com.example.yzvar_telegrambot.repositories.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component("userRoleInitializer")
public class UserRoleInitializer {

    private final UserRoleRepository userRoleRepository;

    public UserRoleInitializer(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @PostConstruct
    public void initRoles() {
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            userRoleRepository.findByName(roleEnum).orElseGet(() -> {
                UserRole role = new UserRole();
                role.setName(roleEnum);
                return userRoleRepository.save(role);
            });
        }
    }
}
