package com.example.yzvar_telegrambot.services.user;

import com.example.yzvar_telegrambot.entities.user.UserRole;
import com.example.yzvar_telegrambot.enums.UserRoleEnum;
import com.example.yzvar_telegrambot.repositories.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@DependsOn("userRoleInitializer")
public class RoleUserCache {

    private final UserRoleRepository repository;
    private final Map<UserRoleEnum, UserRole> roleCache = new EnumMap<>(UserRoleEnum.class);

    @PostConstruct
    private void initCache() {
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            UserRole role = repository.findByName(roleEnum)
                    .orElseThrow(() -> new IllegalStateException("Role not found: " + roleEnum));
            roleCache.put(roleEnum, role);
        }
    }

    public UserRole get(UserRoleEnum roleEnum) {
        return roleCache.get(roleEnum);
    }
}

