package com.example.yzvar_telegrambot.repositories;

import com.example.yzvar_telegrambot.entities.order.Order;
import com.example.yzvar_telegrambot.entities.order.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findAllByStatusOrder(StatusOrder statusOrder);

    List<Order> findAllByStatusOrderAndUser_Username(StatusOrder statusOrder, String username);

}
