package com.example.yzvar_telegrambot.services.facades.admin.command;

import com.example.yzvar_telegrambot.dto.SendMessageDTO;
import com.example.yzvar_telegrambot.enums.OrderStatusEnum;
import com.example.yzvar_telegrambot.services.facades.admin.order.OrderAdminFacade;
import com.example.yzvar_telegrambot.services.facades.admin.product.ProductCreateAdminFacade;
import com.example.yzvar_telegrambot.services.facades.admin.product.ProductEditAdminFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.List;
import java.util.Set;

import static com.example.yzvar_telegrambot.configurations.ClientBotConfig.*;

@Service
@RequiredArgsConstructor
public class CommandHandlerAdminFacade {

    private final ProductEditAdminFacade productEditAdminFacade;
    private final ProductCreateAdminFacade productAdminFacade;
    private final OrderAdminFacade orderAdminFacade;

    private final Set<Long> newProductChatIds;
    private final Set<Long> editProductChatIds;
    private final Set<Long> editOrderChatIds;


    public List<SendMessage> adminCommandHandler(Chat chat, Long chatId, String[] commands) {
        String command = commands[0];
        if (!adminCommandSet.contains(command)) {
            return adminTextDataListening(chatId, commands);
        } else if (command.equalsIgnoreCase(ADMIN_ALL_PRODUCTS_COMMAND)) {
            return productEditAdminFacade.getProductsForAdminByStatus(chatId, 0);
        } else if (command.equalsIgnoreCase(ADMIN_NEW_PRODUCT_COMMAND)) {
            return newProduct(chatId, command);
        } else if (command.equalsIgnoreCase(ADMIN_ORDER_GET_PANEL_COMMAND)) {
            return orderAdminFacade.getAdminOrderMessages(chatId);
        }

        return List.of();
    }

    public List<SendMessage> adminCollBackDataHandler(Chat chat, Long chatId, String[] commands) {
        String command = commands[0];
        if (command.equalsIgnoreCase(ADMIN_EDIT_PRODUCT_CBD) && commands.length == 2) {
            return productEditAdminFacade.showProductEditOptions(chatId, Long.parseLong(commands[1]));
        } else if (editProductCBDSet.contains(command) && commands.length == 2) {
            return editProduct(chatId, commands, true);
        } else if (editProductCBDSet2.contains(command) && commands.length == 2) {
            return editProduct(chatId, commands, false);
        } else if (command.equalsIgnoreCase(ADMIN_ORDER_GET_STATUS_CBD) && commands.length == 2) {
            return orderAdminFacade.getAllOrdersByStatus(chatId, OrderStatusEnum.valueOf(commands[1]));
        } else if (command.equalsIgnoreCase(ADMIN_ORDER_EDIT_CBD) && commands.length == 2) {
            return orderAdminFacade.editOrderParameters(chatId, Long.parseLong(commands[1]));
        } else if (editOrderCBDSet.contains(command)) {
            return editOrder(chatId, commands);
        } else if (command.equalsIgnoreCase(ADMIN_NEW_PRODUCT_CATEGORY_CBD) && commands.length == 2) {
            return newProduct(chatId, commands[1]);
        }
        return List.of();
    }

    public List<SendMessage> adminTextDataListening(Long chatId, String[] commands) {
        if (newProductChatIds.contains(chatId)) {
            return newProduct(chatId, commands[0]);
        } else if (editProductChatIds.contains(chatId)) {
            return editProduct(chatId, commands, false);
        } else if (editOrderChatIds.contains(chatId)) {
            return editOrder(chatId, commands);
        }
        return List.of();
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

    private List<SendMessage> editProduct(Long chatId, String[] commandArr, Boolean step) {
        SendMessageDTO dto;
        if (step) {
            dto = productEditAdminFacade.startProductEditStep(chatId, commandArr[0], Long.parseLong(commandArr[1]));
        } else {
            String text = commandArr.length > 1 ? commandArr[1] : commandArr[0];
            dto = productEditAdminFacade.applyProductEditAndSave(chatId, text);
        }
        if (dto.getStatus()) {
            editProductChatIds.add(chatId);
        }
        return dto.getMessages();
    }

    private List<SendMessage> editOrder(Long chatId, String[] commandArr) {
        var dto = orderAdminFacade.editOrder(chatId, commandArr);
        if (dto.getStatus()) {
            editOrderChatIds.add(chatId);
        } else {
            editOrderChatIds.remove(chatId);
        }
        return dto.getMessages();
    }


}
