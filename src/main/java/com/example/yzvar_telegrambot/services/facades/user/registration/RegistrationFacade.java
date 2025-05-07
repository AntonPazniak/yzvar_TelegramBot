package com.example.yzvar_telegrambot.services.facades.user.registration;

import com.example.yzvar_telegrambot.configurations.ClientBotConfig;
import com.example.yzvar_telegrambot.dto.user.UserRegistrationDTO;
import com.example.yzvar_telegrambot.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationFacade {

    private final UserService userService;

    public List<SendMessage> registrationNewUser(Chat chat) {
        ArrayList<SendMessage> messages = new ArrayList<>();
        if (!userService.existUserById(chat.getId())) {
            String userName = chat.getUserName() != null ? chat.getUserName() : "Unknown";
            String firstName = chat.getFirstName() != null ? chat.getFirstName() : "Unknown";
            String lastName = chat.getLastName() != null ? chat.getLastName() : "Unknown";
            var newUser = UserRegistrationDTO
                    .builder()
                    .id(chat.getId())
                    .firstName(firstName)
                    .lastName(lastName)
                    .username(userName)
                    .build();
            userService.createUser(newUser);
            messages.add(
                    new SendMessage(chat.getId().toString(), ClientBotConfig.START_MESSAGE)
            );
        } else {
            messages.add(
                    new SendMessage(chat.getId().toString(), ClientBotConfig.START_MESSAGE_USER_EXIST)
            );
        }
        return messages;
    }


}
