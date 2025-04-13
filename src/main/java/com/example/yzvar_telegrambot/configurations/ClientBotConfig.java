package com.example.yzvar_telegrambot.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Configuration
@Data
public class ClientBotConfig {

    @Value("${bot.client.name}")
    private String name;

    @Value("${bot.client.token}")
    private String token;


    public static final String START_COMMAND = "/start";
    public static final String START_COMMAND_DESCRIPTION = "Starts the bot";
    public static final String START_MESSAGE = "Welcome to the Klo Frutti";

    public static final String STOP_COMMAND = "/stop";
    public static final String STOP_COMMAND_DESCRIPTION = "Stops the bot";

    public static final String HELP_COMMAND = "/help";
    public static final String HELP_COMMAND_DESCRIPTION = "Shows this help message";

    public static final String SHOW_PRODUCTS_COMMAND = "/all_products";
    public static final String SHOW_PRODUCTS_COMMAND_DESCRIPTION = "Shows products";
    public static final String PRODUCTS_BUTTON_MASSAGE = "Show all products \uD83D\uDCE6";

    public static final String NEW_ORDER_COMMAND = "/new_order";
    public static final String NEW_ORDER_COMMAND_DESCRIPTION = "Order a product";

    public static final String MY_ORDERS_COMMAND = "/my_orders";
    public static final String MY_ORDERS_COMMAND_DESCRIPTION = "Shows my orders";

    public static final String SEND_MESSAGE_COMMAND = "/send_message";
    public static final String SEND_MESSAGE_COMMAND_DESCRIPTION = "Send a message to the administrator";


    /**
     * Order commands
     */

    public static final String ORDER_COMMAND = "/order";
    public static final String ORDER_COMMAND_DESCRIPTION = "Shows menu order";
    public static final String ORDER_BUTTON_MESSAGE = "Get my orders";
    public static final String ORDER_MESSAGE = "Select an option:";

    public static final String ORDER_CANCEL_COMMAND = "/cancel_order";
    public static final String ORDER_CANCEL_COMMAND_DESCRIPTION = "Cancels the order";
    public static final String ORDER_CANCEL_MESSAGE = "Order cancelled";
    public static final String ORDER_CANCEL_BUTTON_MESSAGE = "Cancel ❌";

    public static final String ORDER_MY_PROCESSING_COMMAND = "/order_my_processing";
    public static final String ORDER_MY_PROCESSING_COMMAND_DESCRIPTION = "Get my orders processing";
    public static final String ORDER_MY_PROCESSING_BUTTON_MESSAGE = "Processing";

    public static final String ORDER_MY_CANCELED_COMMAND = "/order_my_cancel";
    public static final String ORDER_MY_CANCELED_COMMAND_DESCRIPTION = "Get my orders canceled";
    public static final String ORDER_MY_CANCELED_BUTTON_MESSAGE = "Canceled";

    public static final String ORDER_MY_COMPLETED_COMMAND = "/order_my_completed";
    public static final String ORDER_MY_COMPLETED_COMMAND_DESCRIPTION = "Get my orders completed";
    public static final String ORDER_MY_COMPLETED_BUTTON_MESSAGE = "Completed";




    public static final KeyboardRow row0 = new KeyboardRow();
    public static final KeyboardRow row1 = new KeyboardRow();
    public static final KeyboardRow row2 = new KeyboardRow();

    public static final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    public static final List<KeyboardRow> keyboard = new ArrayList<>();
    public static final ReplyKeyboardMarkup mainKeyboardMarkup;

    static {
        mainKeyboardMarkup = new ReplyKeyboardMarkup();
        mainKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(SHOW_PRODUCTS_COMMAND));
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(new KeyboardButton(MY_ORDERS_COMMAND));
        keyboard.add(row);
        mainKeyboardMarkup.setKeyboard(keyboard);
        row = new KeyboardRow();
        row.add(new KeyboardButton(SEND_MESSAGE_COMMAND));
        keyboard.add(row);
    }

    public final List<BotCommand> listofCommands = Arrays.asList(
            new BotCommand(START_COMMAND, START_COMMAND_DESCRIPTION),
            new BotCommand(HELP_COMMAND, HELP_COMMAND_DESCRIPTION),
            new BotCommand(SHOW_PRODUCTS_COMMAND, SHOW_PRODUCTS_COMMAND_DESCRIPTION),
            new BotCommand(MY_ORDERS_COMMAND, MY_ORDERS_COMMAND_DESCRIPTION),
            new BotCommand(SEND_MESSAGE_COMMAND, SEND_MESSAGE_COMMAND_DESCRIPTION)
    );

}
