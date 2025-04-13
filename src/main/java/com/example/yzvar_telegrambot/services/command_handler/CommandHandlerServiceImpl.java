package com.example.yzvar_telegrambot.services.command_handler;

import com.example.yzvar_telegrambot.configurations.ClientBotConfig;
import com.example.yzvar_telegrambot.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandHandlerServiceImpl implements CommandHandlerService {

    private final UserService userService;

    public ArrayList<SendMessage> onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return commandHandler(
                    update.getMessage().getChat(),
                    update.getMessage().getChatId(),
                    new String[]{update.getMessage().getText()}
            );
        } else if (update.hasCallbackQuery()) {
            return commandHandlerOnCollBack(update);
        } else {
            throw new RuntimeException("No such command: " + update.getMessage().getText());
        }
    }

    private ArrayList<SendMessage> commandHandlerOnCollBack(Update update) {
        return commandHandler(
                null,
                update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getData().split(":")
        );
    }

    private ArrayList<SendMessage> commandHandler(Chat chat, Long chatId, String[] commandArr) {
        String command = commandArr[0];
        if (command.equalsIgnoreCase(ClientBotConfig.START_COMMAND)) {
            userService.createUserIfNotExist(chat);
            ArrayList<SendMessage> sendMessages = new ArrayList<>();
            var message = new SendMessage(chat.getId().toString(), ClientBotConfig.START_MESSAGE);
            sendMessages.add(message);
            return sendMessages;
        }
        return new ArrayList<SendMessage>() {{}};
    }


}
