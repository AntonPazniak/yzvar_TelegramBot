package com.example.yzvar_telegrambot.services.facades;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface CommandHandlerService {

    List<SendMessage> onUpdateReceived(Update update);
}
