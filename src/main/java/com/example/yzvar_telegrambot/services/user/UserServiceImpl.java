package com.example.yzvar_telegrambot.services.user;


import com.example.yzvar_telegrambot.dto.user.UserDTO;
import com.example.yzvar_telegrambot.dto.user.UserRegistrationDTO;
import com.example.yzvar_telegrambot.entities.user.User;
import com.example.yzvar_telegrambot.enums.UserRoleEnum;
import com.example.yzvar_telegrambot.mapper.UserMapper;
import com.example.yzvar_telegrambot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleUserCache roleCacheService;

    @Override
    public UserDTO createUser(UserRegistrationDTO userRegistrationDTO) {
        User newUser = User.builder()
                .id(userRegistrationDTO.getId())
                .username(userRegistrationDTO.getUsername())
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .phone(userRegistrationDTO.getPhone())
                .roles(Collections.singleton(roleCacheService.get(UserRoleEnum.USER)))
                .build();
        userRepository.save(newUser);

        return UserMapper.toDto(newUser);
    }


    @Cacheable(value = "users", key = "#id")
    public User getUserByIdOrElseThrow(Long id) {
        return userRepository.findById(id).orElseThrow();
    }


    //    @Cacheable(value = "users", key = "#id")
    public Boolean existUserById(Long id) {
        return userRepository.existsById(id);
    }

    @Cacheable(value = "users", key = "#id")
    public Boolean isUserAdminById(Long id) {
        var user = userRepository.findById(id);
        return user.map(
                        value -> value.getRoles()
                                .contains(roleCacheService.get(UserRoleEnum.ADMIN)))
                .orElse(false);
    }

}
