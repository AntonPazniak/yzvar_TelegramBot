package com.example.yzvar_telegrambot.mapper;

import com.example.yzvar_telegrambot.dto.user.UserDTO;
import com.example.yzvar_telegrambot.entities.user.User;

public class UserMapper {

    public static UserDTO toDto(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

}
