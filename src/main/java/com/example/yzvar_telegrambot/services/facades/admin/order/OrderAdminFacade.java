package com.example.yzvar_telegrambot.services.facades.admin.order;

import com.example.yzvar_telegrambot.dto.SendMessageDTO;
import com.example.yzvar_telegrambot.dto.order.OrderDTO;
import com.example.yzvar_telegrambot.dto.order.OrderEditStepDTO;
import com.example.yzvar_telegrambot.enums.OrderEditStepEnum;
import com.example.yzvar_telegrambot.enums.OrderStatusEnum;
import com.example.yzvar_telegrambot.services.order.OrderService;
import com.example.yzvar_telegrambot.services.order.StatusOrderCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.yzvar_telegrambot.configurations.ClientBotConfig.*;

@Service
@RequiredArgsConstructor
public class OrderAdminFacade {

    private final OrderService orderService;
    private final StatusOrderCache statusOrderCache;
    private final Map<Long, OrderEditStepDTO> editOrderMap = new HashMap<>();

    public List<SendMessage> getAdminOrderMessages(Long chatId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            var button = new InlineKeyboardButton(status.name());
            button.setCallbackData(ADMIN_ORDER_GET_STATUS_CBD + ":" + status.name());
            rows.add(List.of(button));
        }
        var message = new SendMessage(chatId.toString(), ADMIN_ORDER_GET_PANEL_TEXT);
        message.setReplyMarkup(new InlineKeyboardMarkup(rows));
        return List.of(message);
    }

    public List<SendMessage> getAllOrdersByStatus(Long chatId, OrderStatusEnum status) {
        return convertOrderDTOToSendMessage(chatId,
                orderService.getAllOrdersByStatus(status)
        );
    }

    public List<SendMessage> getAllOrdersByStatusAndUsername(Long chatId, OrderStatusEnum status, String username) {
        return convertOrderDTOToSendMessage(chatId,
                orderService.getAllOrdersByStatusAndUsername(status, username)
        );
    }

    public List<SendMessage> editOrderParameters(Long chatId, Long orderId) {
        var dto = orderService.getOrderById(orderId);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (OrderEditStepEnum orderEditStep : OrderEditStepEnum.values()) {
            var button = new InlineKeyboardButton(orderEditStep.name());
            button.setCallbackData(editOrderParamMap.get(orderEditStep) + ":" + dto.getId());
            rows.add(List.of(button));
        }
        var message = new SendMessage(chatId.toString(), ADMIN_ORDER_EDIT_PARAMS_TEXT);
        message.setReplyMarkup(new InlineKeyboardMarkup(rows));
        return List.of(message);
    }

    public SendMessageDTO editOrder(Long chatId, String[] commands) {
        if (editOrderMap.containsKey(chatId)) {
            var dto = editOrderMap.get(chatId);
            return switch (dto.getStep()) {
                case price -> editOrderAndSave(chatId, commands[0], dto.getOrderDTO());
                case status -> editOrderStatusSave(chatId, commands[1], dto.getOrderDTO());
            };
        } else {
            if (commands[0].equalsIgnoreCase(ADMIN_ORDER_EDIT_PRICE_CBD)) {
                return editOrderStart(chatId, commands[1]);
            } else if (commands[0].equalsIgnoreCase(ADMIN_ORDER_EDIT_STATUS_CBD)) {
                return editOrderStatus(chatId, commands[1]);
            }
        }
        return new SendMessageDTO();
    }

    private SendMessageDTO editOrderStart(Long chatId, String text) {
        Long orderId = Long.parseLong(text);
        OrderDTO orderDto = orderService.getOrderById(orderId);
        editOrderMap.put(
                chatId,
                new OrderEditStepDTO(OrderEditStepEnum.price, orderDto)
        );
        return new SendMessageDTO(
                true,
                List.of(new SendMessage(chatId.toString(), ADMIN_ORDER_EDIT_PRICE_TEXT))
        );
    }

    private SendMessageDTO editOrderAndSave(Long chatId, String priceS, OrderDTO orderDTO) {
        try {
            Float price = Float.parseFloat(priceS);
            orderDTO.setPrice(price);
            var dto = orderService.updateOrderPrice(orderDTO);
            editOrderMap.remove(chatId);
            return new SendMessageDTO(false,
                    List.of(new SendMessage(chatId.toString(), dto.toString()))
            );
        } catch (NumberFormatException e) {
            return new SendMessageDTO(true,
                    List.of(new SendMessage(chatId.toString(), ADMIN_ORDER_EDIT_PRICE_ERROR_TEXT))
            );
        }
    }

    private SendMessageDTO editOrderStatus(Long chatId, String text) {
        Long orderId = Long.parseLong(text);
        OrderDTO orderDto = orderService.getOrderById(orderId);
        editOrderMap.put(chatId, new OrderEditStepDTO(OrderEditStepEnum.status, orderDto));
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            var button = new InlineKeyboardButton(status.name());
            button.setCallbackData(ADMIN_ORDER_EDIT_STATUS_SET_CBD + ":" + status.name());
            rows.add(List.of(button));
        }
        SendMessage message = new SendMessage(chatId.toString(), ADMIN_ORDER_EDIT_STATUS_TEXT);
        message.setReplyMarkup(new InlineKeyboardMarkup(rows));

        return new SendMessageDTO(true, List.of(message));
    }

    private SendMessageDTO editOrderStatusSave(Long chatId, String text, OrderDTO dto) {
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(text);
        dto.setOrderStatus(orderStatusEnum);
        dto = orderService.updateStatusOrder(dto);
        editOrderMap.remove(chatId);
        return new SendMessageDTO(false, List.of(new SendMessage(chatId.toString(), dto.toString())
        ));
    }


    private List<SendMessage> convertOrderDTOToSendMessage(Long chatId, List<OrderDTO> orders) {
        return orders.stream().map(dto -> {
            SendMessage sendMessage = new SendMessage(chatId.toString(), dto.toString());
            var button = new InlineKeyboardButton(EDIT_TEXT_BUTTON);
            button.setCallbackData(ADMIN_ORDER_EDIT_CBD + ":" + dto.getId());
            sendMessage.setReplyMarkup(new InlineKeyboardMarkup(
                    List.of(List.of(button))
            ));
            return sendMessage;
        }).toList();
    }

}
