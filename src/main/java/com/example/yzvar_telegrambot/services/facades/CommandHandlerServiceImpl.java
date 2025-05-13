package com.example.yzvar_telegrambot.services.facades;

import com.example.yzvar_telegrambot.services.facades.admin.command.CommandHandlerAdminFacade;
import com.example.yzvar_telegrambot.services.facades.user.command.CommandHandlerUserFacade;
import com.example.yzvar_telegrambot.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandHandlerServiceImpl implements CommandHandlerService {

    private final UserService userService;

    private final CommandHandlerAdminFacade commandHandlerAdminFacade;
    private final CommandHandlerUserFacade commandHandlerUserFacade;

    public List<SendMessage> onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return commandHandler(
                    update.getMessage().getChat(),
                    update.getMessage().getChatId(),
                    new String[]{update.getMessage().getText()},
                    true
            );
        } else if (update.hasCallbackQuery()) {
            return commandHandler(
                    null,
                    update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getData().split(":"),
                    false
            );
        } else {
            throw new RuntimeException("No such command: " + update.getMessage().getText());
        }
    }


    private List<SendMessage> commandHandler(Chat chat, Long chatId, String[] commandArr, boolean isCommand) {
        if (userService.isUserAdminById(chatId)) {
            if (isCommand)
                return commandHandlerAdminFacade.adminCommandHandler(chat, chatId, commandArr);
            else
                return commandHandlerAdminFacade.adminCollBackDataHandler(chat, chatId, commandArr);
        } else {
            if (isCommand)
                return commandHandlerUserFacade.userCommandHandler(chat, chatId, commandArr);
            else
                return commandHandlerUserFacade.userCollBackDataHandler(chat, chatId, commandArr);
        }
    }


}
