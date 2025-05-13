package com.example.yzvar_telegrambot.services.facades.user.command;


import com.example.yzvar_telegrambot.services.facades.user.order.OrderFacade;
import com.example.yzvar_telegrambot.services.facades.user.product.ProductFacade;
import com.example.yzvar_telegrambot.services.facades.user.registration.RegistrationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.List;

import static com.example.yzvar_telegrambot.configurations.ClientBotConfig.*;

@Service
@RequiredArgsConstructor
public class CommandHandlerUserFacade {


    private final RegistrationFacade registrationFacade;
    private final ProductFacade productFacade;
    private final OrderFacade orderFacade;

    public List<SendMessage> userCommandHandler(Chat chat, Long chatId, String[] commands) {
        String command = commands[0];
        if (command.equalsIgnoreCase(START_COMMAND)) {
            return registrationFacade.registrationNewUser(chat);
        } else if (command.equalsIgnoreCase(SHOW_PRODUCTS_COMMAND)) {
            return productFacade.getAllProducts(chatId);
        } else if (command.equalsIgnoreCase(MY_ORDERS_COMMAND)) {
            return orderFacade.getPanelOrder(chatId);
        }
        return List.of();
    }

    public List<SendMessage> userCollBackDataHandler(Chat chat, Long chatId, String[] commands) {
        String command = commands[0];
        if (command.equalsIgnoreCase(NEW_ORDER_CBD) && commands.length == 2) {
            return orderFacade.newOrder(chatId, Long.parseLong(commands[1]));
        } else if (command.equalsIgnoreCase(MY_ORDERS_SHOW_CBD) && commands.length == 2) {
            return orderFacade.getOrdesUserByStatus(chatId, commands[1]);
        }
        return List.of();
    }

    public List<SendMessage> userTextDataListening(Long chatId, String[] commands) {

        return List.of();
    }

}
