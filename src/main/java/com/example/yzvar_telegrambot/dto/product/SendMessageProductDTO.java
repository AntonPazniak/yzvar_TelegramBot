package com.example.yzvar_telegrambot.dto.product;


import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Data
@Builder
public class SendMessageProductDTO {
    private Boolean status;
    private List<SendMessage> messages;
}
