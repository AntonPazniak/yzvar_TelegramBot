package com.example.yzvar_telegrambot.services.facades.user.order;

import com.example.yzvar_telegrambot.dto.order.OrderCreateDTO;
import com.example.yzvar_telegrambot.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

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

}
