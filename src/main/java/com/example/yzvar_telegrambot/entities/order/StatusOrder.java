package com.example.yzvar_telegrambot.entities.order;

import com.example.yzvar_telegrambot.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class StatusOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private OrderStatusEnum name;

    @OneToMany(mappedBy = "statusOrder", fetch = FetchType.LAZY)
    private List<Order> orders;


}
