package com.example.yzvar_telegrambot.componets.initializers;

import com.example.yzvar_telegrambot.entities.order.StatusOrder;
import com.example.yzvar_telegrambot.enums.StatusOrderEnum;
import com.example.yzvar_telegrambot.repositories.StatusOrderRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("statusOrderInitializer")
@RequiredArgsConstructor
public class StatusOrderInitializer {

    private final StatusOrderRepository statusOrderRepository;

    @PostConstruct
    public void initStatuses() {
        for (StatusOrderEnum statusOrderEnum : StatusOrderEnum.values()) {
            statusOrderRepository.findByName(statusOrderEnum).orElseGet(() -> {
                StatusOrder status = StatusOrder.builder().name(statusOrderEnum).build();
                return statusOrderRepository.save(status);
            });
        }
    }

}
