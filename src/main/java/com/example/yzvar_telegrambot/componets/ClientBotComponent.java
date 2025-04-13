package com.example.yzvar_telegrambot.componets;

import com.example.yzvar_telegrambot.services.command_handler.CommandHandlerService;
import com.example.yzvar_telegrambot.configurations.ClientBotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class ClientBotComponent extends TelegramLongPollingBot {

    @Autowired
    private CommandHandlerService commandHandlerService;
    private final ClientBotConfig config;

    // Map to store sent message IDs by chatId
    private final Map<Long, List<Integer>> chatMessageIds = new HashMap<>();

    public ClientBotComponent(ClientBotConfig config, CommandHandlerService commandHandlerService) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(config.listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: {}", e.getMessage());
        }
        this.commandHandlerService = commandHandlerService;
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            int messageId = update.getMessage().getMessageId();
            storeMessageId(update.getMessage().getChatId(), messageId);
        }
        sendMessage(commandHandlerService.onUpdateReceived(update));
    }

    // send a message and delete old messages
    public void sendMessage(ArrayList<SendMessage> messages) {
        if (messages.isEmpty()) {
            return;
        }
        Long chatId = Long.parseLong(messages.getFirst().getChatId());

        // Delete old messages
        deleteOldMessages(chatId);

        // Send new messages and store their IDs
        messages.forEach(message -> {
            message.enableMarkdown(true);
            try {
                int messageId = execute(message).getMessageId();
                storeMessageId(chatId, messageId);
            } catch (TelegramApiException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void deleteMessage(Long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId.toString());
        deleteMessage.setMessageId(messageId);

        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error("Error deleting message", e);
        }
    }

    private void executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("ERROR_TEXT{}", e.getMessage());
        }
    }

    private void storeMessageId(Long chatId, int messageId) {
        chatMessageIds.computeIfAbsent(chatId, k -> new ArrayList<>()).add(messageId);
    }

    private void deleteOldMessages(Long chatId) {
        List<Integer> messageIds = chatMessageIds.get(chatId);
        if (messageIds != null) {
            for (int messageId : messageIds) {
                deleteMessage(chatId, messageId);
            }
            chatMessageIds.remove(chatId);
        }
    }
}
