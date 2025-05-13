package com.example.yzvar_telegrambot.services.facades.user.order;

import com.example.yzvar_telegrambot.dto.order.OrderCreateDTO;
import com.example.yzvar_telegrambot.enums.OrderStatusEnum;
import com.example.yzvar_telegrambot.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.yzvar_telegrambot.configurations.ClientBotConfig.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;

    public List<SendMessage> newOrder(Long idUser, Long idProduct) {
        var order = orderService.createOrder(OrderCreateDTO.builder()
                .userId(idUser)
                .productId(idProduct)
                .quantity(1)
                .build());
        return List.of(new SendMessage(idUser.toString(), order.toString()));
    }

    public List<SendMessage> getPanelOrder(Long idUser) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            var button = new InlineKeyboardButton(orderStatusEnum.name());
            button.setCallbackData(MY_ORDERS_SHOW_CBD + ":" + orderStatusEnum.name());
            rows.add(List.of(button));
        }
        SendMessage message = new SendMessage(idUser.toString(), "Order Panel");
        message.setReplyMarkup(new InlineKeyboardMarkup(rows));
        return List.of(message);
    }

    public List<SendMessage> getOrdesUserByStatus(Long chatId, String status) {
        return orderService.getAllOrdersByIdUserAndStatus(chatId, OrderStatusEnum.valueOf(status))
                .stream().map(orderDTO -> {
                    SendMessage message = new SendMessage(chatId.toString(), orderDTO.toString());
                    var button = new InlineKeyboardButton(EDIT_TEXT_BUTTON);
                    button.setCallbackData(MY_ORDERS_EDIT_CBD + ":" + orderDTO.getId());
                    message.setReplyMarkup(
                            new InlineKeyboardMarkup(
                                    List.of(
                                            List.of(button)
                                    )
                            )
                    );
                    return message;
                }).toList();
    }


}
