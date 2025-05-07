package com.example.yzvar_telegrambot.services.user;

import com.example.yzvar_telegrambot.dto.user.UserDTO;
import com.example.yzvar_telegrambot.dto.user.UserRegistrationDTO;

public interface UserService {
    UserDTO createUser(UserRegistrationDTO userRegistrationDTO);

    Boolean existUserById(Long id);

    Boolean isUserAdminById(Long id);
}
