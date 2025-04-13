package com.example.yzvar_telegrambot.repositories;

import com.example.yzvar_telegrambot.entities.order.StatusOrder;
import com.example.yzvar_telegrambot.enums.StatusOrderEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusOrderRepository extends JpaRepository<StatusOrder, Long> {
    Optional<StatusOrder> findByName(StatusOrderEnum statusOrderEnum);
}
