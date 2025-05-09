package com.example.yzvar_telegrambot.services.facades;

import com.example.yzvar_telegrambot.services.facades.admin.product.ProductCreateAdminFacade;
import com.example.yzvar_telegrambot.services.facades.admin.product.ProductEditAdminFacade;
import com.example.yzvar_telegrambot.services.facades.user.order.OrderFacade;
import com.example.yzvar_telegrambot.services.facades.user.product.ProductFacade;
import com.example.yzvar_telegrambot.services.facades.user.registration.RegistrationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.yzvar_telegrambot.configurations.ClientBotConfig.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandHandlerServiceImpl implements CommandHandlerService {

    private final RegistrationFacade registrationFacade;
    private final ProductFacade productFacade;
    private final OrderFacade orderFacade;

    private final ProductEditAdminFacade productEditAdminFacade;
    private final ProductCreateAdminFacade productAdminFacade;

    private final Set<Long> newProductChatIds;
    private final Set<Long> editProductChatIds;

    public List<SendMessage> onUpdateReceived(Update update) {
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

    private List<SendMessage> commandHandlerOnCollBack(Update update) {
        return commandHandler(
                null,
                update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getData().split(":")
        );
    }

    private List<SendMessage> commandHandler(Chat chat, Long chatId, String[] commandArr) {
        String command = commandArr[0];
        if (command.equalsIgnoreCase(START_COMMAND)) {
            return registrationFacade.registrationNewUser(chat);
        } else if (command.equalsIgnoreCase(NEW_PRODUCT_COMMAND) || newProductChatIds.contains(chatId)) {
            return newProduct(chatId, commandArr[0]);
        } else if (command.equalsIgnoreCase(SHOW_PRODUCTS_COMMAND)) {
            return productFacade.getAllProducts(chatId);
        } else if (command.equalsIgnoreCase(NEW_ORDER_COMMAND) && commandArr.length == 2) {
            return orderFacade.newOrder(chatId, Long.parseLong(commandArr[1]));
        } else if (command.equalsIgnoreCase(GET_ALL_PRODUCTS_ADMIN_COMMAND)) {
            return productEditAdminFacade.getProductsForAdminByStatus(chatId, 0);
        } else if (command.equalsIgnoreCase(EDIT_PRODUCT_COMMAND) && commandArr.length == 2) {
            return productEditAdminFacade.showProductEditOptions(chatId, Long.parseLong(commandArr[1]));
        } else if (command.equalsIgnoreCase(EDIT_PRODUCT_TITLE_COMMAND)
                || command.equalsIgnoreCase(EDIT_PRODUCT_DESCRIPTION_COMMAND)
                || command.equalsIgnoreCase(EDIT_PRODUCT_PRICE_COMMAND)
                || command.equalsIgnoreCase(EDIT_PRODUCT_STATUS_COMMAND)
                || command.equalsIgnoreCase(EDIT_PRODUCT_CATEGORY_COMMAND)
                || command.equalsIgnoreCase(EDIT_PRODUCT_WEIGHT_COMMAND)
                && commandArr.length == 2) {
            var dto = productEditAdminFacade.startProductEditStep(chatId, command, Long.parseLong(commandArr[1]));
            if (dto.getStatus()) {
                editProductChatIds.add(chatId);
            }
            return dto.getMessages();
        } else if (editProductChatIds.contains(chatId)) {
            String text = commandArr.length > 1 ? commandArr[1] : commandArr[0];
            var dto = productEditAdminFacade.applyProductEditAndSave(chatId, text);
            if (dto.getStatus()) {
                editProductChatIds.add(chatId);
            }
            return dto.getMessages();
        }
        return new ArrayList<SendMessage>() {
        };
    }

    private List<SendMessage> newProduct(Long chatId, String text) {
        var dto = productAdminFacade.createProduct(chatId, text);
        if (dto.getStatus()) {
            newProductChatIds.add(chatId);
        } else {
            newProductChatIds.remove(chatId);
        }
        return dto.getMessages();
    }


}
