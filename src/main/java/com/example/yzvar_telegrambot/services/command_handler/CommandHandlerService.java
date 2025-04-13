package com.example.yzvar_telegrambot.services.command_handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

public interface CommandHandlerService {

    ArrayList<SendMessage> onUpdateReceived(Update update);
}
