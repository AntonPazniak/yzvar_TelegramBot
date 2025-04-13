package com.example.yzvar_telegrambot.services.user;

import com.example.yzvar_telegrambot.dto.user.UserDTO;
import org.telegram.telegrambots.meta.api.objects.Chat;

public interface UserService {
    UserDTO createUserIfNotExist(Chat chat);
}
