package com.example.yzvar_telegrambot.services.order;

import com.example.yzvar_telegrambot.entities.order.StatusOrder;
import com.example.yzvar_telegrambot.enums.OrderStatusEnum;
import com.example.yzvar_telegrambot.repositories.StatusOrderRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@DependsOn("statusOrderInitializer")
public class StatusOrderCache {

    private final StatusOrderRepository statusOrderRepository;
    private final Map<OrderStatusEnum, StatusOrder> statusCache = new EnumMap<>(OrderStatusEnum.class);

    @PostConstruct
    private void initCache() {
        for (OrderStatusEnum statusOrderEnum : OrderStatusEnum.values()) {
            StatusOrder statusOrder = statusOrderRepository.findByName(statusOrderEnum)
                    .orElseThrow(() -> new IllegalStateException("Role not found: " + statusOrderEnum));
            statusCache.put(statusOrderEnum, statusOrder);
        }
    }

    public StatusOrder get(OrderStatusEnum statusOrderEnum) {
        return statusCache.get(statusOrderEnum);
    }
}
