package com.example.yzvar_telegrambot.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageEditProductDTO {
    private Boolean status = false;
    private List<SendMessage> messages = new ArrayList<>();
}
