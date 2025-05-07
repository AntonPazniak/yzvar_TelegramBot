package com.example.yzvar_telegrambot.dto.order;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderDTO {
    LocalDateTime orderDate;
    String title;
    Float price;

}
