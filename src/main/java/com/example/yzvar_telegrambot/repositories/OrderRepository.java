package com.example.yzvar_telegrambot.repositories;

import com.example.yzvar_telegrambot.entities.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
