package com.example.yzvar_telegrambot.configurations;

import com.example.yzvar_telegrambot.enums.OrderEditStepEnum;
import com.example.yzvar_telegrambot.enums.ProductEditStepEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

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
    public static final String START_MESSAGE_USER_EXIST = "You are already on the server";


    public static final String STOP_COMMAND = "/stop";
    public static final String STOP_COMMAND_DESCRIPTION = "Stops the bot";

    public static final String HELP_COMMAND = "/help";
    public static final String HELP_COMMAND_DESCRIPTION = "Shows this help message";

    public static final String SHOW_PRODUCTS_COMMAND = "/all_products";
    public static final String SHOW_PRODUCTS_COMMAND_DESCRIPTION = "Shows products";
    public static final String PRODUCTS_BUTTON_MASSAGE = "Show all products \uD83D\uDCE6";

    public static final String NEW_ORDER_CBD = "/new_order";

    public static final String NEW_ORDER_COMMAND_DESCRIPTION = "Order a product";

    /**
     * User Order commands
     */

    public static final String MY_ORDERS_COMMAND = "/my_orders";
    public static final String MY_ORDERS_COMMAND_DESCRIPTION = "Shows my orders";
    public static final String MY_ORDERS_SHOW_CBD = "/my_orders";
    public static final String MY_ORDERS_EDIT_CBD = "/order_edit";
    public static final String ORDER_BUTTON_TEXT = "Order \uD83D\uDCE6";
    public static final String CANCEL_BUTTON_TEXT = "Cancel ❌";

    public static final String EDIT_TEXT_BUTTON = "Edit";


    /**
     * Order Admin
     */

    public static final String ADMIN_ORDER_GET_PANEL_COMMAND = "/admin_order_get_panel";
    public static final String ADMIN_ORDER_GET_PANEL_TEXT = "Please select a status order.";
    public static final String ADMIN_ORDER_GET_STATUS_CBD = "/admin_order_get_status";
    public static final String ADMIN_ORDER_EDIT_CBD = "/admin_order_edit";

    public static final String ADMIN_ORDER_EDIT_PARAMS_TEXT = "Please select a parameter witch you want to edit.";
    public static final String ADMIN_ORDER_EDIT_PRICE_CBD = "/admin_order_edit_price";
    public static final String ADMIN_ORDER_EDIT_STATUS_CBD = "/admin_order_edit_status";
    public static final String ADMIN_ORDER_EDIT_STATUS_SET_CBD = "/admin_order_edit_status_set";

    public static final String ADMIN_ORDER_EDIT_STATUS_TEXT = "Please select a status order.";
    public static final String ADMIN_ORDER_EDIT_PRICE_TEXT = "Please enter a new price.";
    public static final String ADMIN_ORDER_EDIT_PRICE_ERROR_TEXT = "Error: Please enter price as a number, such as 12.99.";

    public static final Map<OrderEditStepEnum, String> editOrderParamMap = Map.ofEntries(
            Map.entry(OrderEditStepEnum.price, ADMIN_ORDER_EDIT_PRICE_CBD),
            Map.entry(OrderEditStepEnum.status, ADMIN_ORDER_EDIT_STATUS_CBD)
    );

    /**
     * new product
     */

    // new product massage
    public static final String ADMIN_NEW_PRODUCT_TITLE_TEXT = "New Product \nEnter the title";
    public static final String ADMIN_NEW_PRODUCT_DESCRIPTION_TEXT = "New Product \nEnter the description";
    public static final String ADMIN_NEW_PRODUCT_CATEGORY_TEXT = "New Product \nSelect the category";


    public static final String ADMIN_NEW_PRODUCT_COMMAND = "/new_product";
    public static final String ADMIN_NEW_PRODUCT_CANCEL_CBD = "/new_product_cancel";

    public static final String ADMIN_NEW_PRODUCT_CATEGORY_CBD = "/new_product_category";

    public static final String ADMIN_NEW_PRODUCT_PRICE_TEXT = "New Product \nEnter the price";
    public static final String ADMIN_NEW_PRODUCT_PRICE_ERROR_TEXT = "Error: Please enter price as a number, such as 12.99.";

    public static final String ADMIN_NEW_PRODUCT_CANCEL_TEXT = "You cancel the crete a new product";
    public static final String ADMIN_NEW_PRODUCT_WEIGHT_TEXT = "New Product \nEnter the weight of the product";
    public static final String ADMIN_NEW_PRODUCT_WEIGHT_ERROR_TEXT = "Error: Please enter weight as a number, such as 100.";
    public static final String ADMIN_NEW_PRODUCT_SUCCESS_TEXT = "New product successfully created";

    /**
     *  edit product
     */

    public static final String ADMIN_ALL_PRODUCTS_COMMAND = "/get_all_admin_products";

    public static final String ADMIN_EDIT_PRODUCT_CBD = "/edit_product";
    public static final String ADMIN_EDIT_PRODUCT_TITLE_CBD = "/edit_product_title";
    public static final String ADMIN_EDIT_PRODUCT_DESCRIPTION_CBD = "/edit_product_description";
    public static final String ADMIN_EDIT_PRODUCT_STATUS_CBD = "/edit_product_status";
    public static final String ADMIN_EDIT_PRODUCT_PRICE_CBD = "/edit_product_price";
    public static final String ADMIN_EDIT_PRODUCT_WEIGHT_CBD = "/edit_product_weight";
    public static final String ADMIN_EDIT_PRODUCT_CATEGORY_CBD = "/edit_product_category";

    public static final String ADMIN_EDIT_PRODUCT_TITLE_TEXT_BUTTON = "Edit title.";
    public static final String ADMIN_EDIT_PRODUCT_DESCRIPTION_TEXT_BUTTON = "Edit description.";
    public static final String ADMIN_EDIT_PRODUCT_STATUS_TEXT_BUTTON = "Edit status.";
    public static final String ADMIN_EDIT_PRODUCT_PRICE_TEXT_BUTTON = "Edit price.";
    public static final String ADMIN_EDIT_PRODUCT_WEIGHT_TEXT_BUTTON = "Edit weight.";
    public static final String ADMIN_EDIT_PRODUCT_CATEGORY_TEXT_BUTTON = "Edit category.";

    public static final String ADMIN_EDIT_PRODUCT_TITLE_TEXT = "Edit product title \nEnter the new title.";
    public static final String ADMIN_EDIT_PRODUCT_DESCRIPTION_TEXT = "Edit product description \nEnter the new description.";
    public static final String ADMIN_EDIT_PRODUCT_STATUS_TEXT = "Edit product status \nSelect the new product status.";
    public static final String ADMIN_EDIT_PRODUCT_PRICE_TEXT = "Edit product price \nEnter the new price.";
    public static final String ADMIN_EDIT_PRODUCT_WEIGHT_TEXT = "Edit product weight \nEnter the new weight.";
    public static final String ADMIN_EDIT_PRODUCT_CATEGORY_TEXT = "Edit product category \nSelect the new product category.";

    public static final String ADMIN_EDIT_PRODUCT_PRICE_ERROR_TEXT = "Error: Please enter a valid price.";
    public static final String ADMIN_EDIT_PRODUCT_WEIGHT_ERROR_TEXT = "Error: Please enter a valid weight.";

    public static final String ADMIN_EDIT_PRODUCT_CATEGORY_SET_CBD = "/edit_product_category_new";
    public static final String ADMIN_EDIT_PRODUCT_STATUS_SET_CBD = "/edit_product_status_new";

    public static final String EDIT_PRODUCT_STATUS_VISIBLE_TEXT_BUTTON = "Visible";
    public static final String EDIT_PRODUCT_STATUS_INVISIBLE_TEXT_BUTTON = "Invisible";

    public static final Set<String> editProductCBDSet2 = Set.of(
            ADMIN_EDIT_PRODUCT_CATEGORY_SET_CBD,
            ADMIN_EDIT_PRODUCT_STATUS_SET_CBD
    );



    public static final Set<String> adminCommandSet = Set.of(
            //order
            ADMIN_ORDER_GET_PANEL_COMMAND,
            //product
            ADMIN_NEW_PRODUCT_COMMAND,
            ADMIN_ALL_PRODUCTS_COMMAND
    );

    public static final Set<String> adminCBDSet = Set.of(
            ADMIN_ORDER_GET_STATUS_CBD,
            // edit product
            ADMIN_EDIT_PRODUCT_CBD,
            ADMIN_EDIT_PRODUCT_TITLE_CBD,
            ADMIN_EDIT_PRODUCT_DESCRIPTION_CBD,
            ADMIN_EDIT_PRODUCT_STATUS_CBD,
            ADMIN_EDIT_PRODUCT_PRICE_CBD,
            ADMIN_EDIT_PRODUCT_WEIGHT_CBD,
            ADMIN_EDIT_PRODUCT_CATEGORY_CBD
    );

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
        keyboard.add(row);
    }

    public final List<BotCommand> defaultCommands = Arrays.asList(
            new BotCommand(START_COMMAND, START_COMMAND_DESCRIPTION),
            new BotCommand(HELP_COMMAND, HELP_COMMAND_DESCRIPTION),
            new BotCommand(SHOW_PRODUCTS_COMMAND, SHOW_PRODUCTS_COMMAND_DESCRIPTION),
            new BotCommand(MY_ORDERS_COMMAND, MY_ORDERS_COMMAND_DESCRIPTION)
    );

    public static InlineKeyboardMarkup getInlineKeyboardMarkupWithCancelButton() {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(List.of(List.of(getCancelButton())));
        return markupInLine;
    }


    public static InlineKeyboardButton getCancelButton() {
        var canselButton = new InlineKeyboardButton(CANCEL_BUTTON_TEXT);
        canselButton.setCallbackData(ADMIN_NEW_PRODUCT_CANCEL_CBD);
        return canselButton;
    }

    public static final List<List<String>> callbackList = List.of(
            List.of(ADMIN_EDIT_PRODUCT_TITLE_CBD, ADMIN_EDIT_PRODUCT_TITLE_TEXT_BUTTON),
            List.of(ADMIN_EDIT_PRODUCT_DESCRIPTION_CBD, ADMIN_EDIT_PRODUCT_DESCRIPTION_TEXT_BUTTON),
            List.of(ADMIN_EDIT_PRODUCT_STATUS_CBD, ADMIN_EDIT_PRODUCT_STATUS_TEXT_BUTTON),
            List.of(ADMIN_EDIT_PRODUCT_PRICE_CBD, ADMIN_EDIT_PRODUCT_PRICE_TEXT_BUTTON),
            List.of(ADMIN_EDIT_PRODUCT_WEIGHT_CBD, ADMIN_EDIT_PRODUCT_WEIGHT_TEXT_BUTTON),
            List.of(ADMIN_EDIT_PRODUCT_CATEGORY_CBD, ADMIN_EDIT_PRODUCT_CATEGORY_TEXT_BUTTON)
    );

    public static final Map<ProductEditStepEnum, String> editProductMessagesByStepMap = Map.ofEntries(
            Map.entry(ProductEditStepEnum.title, ADMIN_EDIT_PRODUCT_TITLE_CBD),
            Map.entry(ProductEditStepEnum.description, ADMIN_EDIT_PRODUCT_DESCRIPTION_TEXT),
            Map.entry(ProductEditStepEnum.status, ADMIN_EDIT_PRODUCT_STATUS_CBD),
            Map.entry(ProductEditStepEnum.price, ADMIN_EDIT_PRODUCT_PRICE_CBD),
            Map.entry(ProductEditStepEnum.weight, ADMIN_EDIT_PRODUCT_WEIGHT_CBD),
            Map.entry(ProductEditStepEnum.category, ADMIN_EDIT_PRODUCT_CATEGORY_TEXT)
    );

    @Data
    @AllArgsConstructor
    public static class EditStepMessage {
        private ProductEditStepEnum productEditStep;
        private String message;
        private String collBackData;

        public EditStepMessage(ProductEditStepEnum productEditStep, String message) {
            this.productEditStep = productEditStep;
            this.message = message;
        }
    }


    public static final Map<String, EditStepMessage> editStepMessageMap = Map.ofEntries(
            Map.entry(ADMIN_EDIT_PRODUCT_TITLE_CBD, new EditStepMessage(ProductEditStepEnum.title, ADMIN_EDIT_PRODUCT_TITLE_TEXT)),
            Map.entry(ADMIN_EDIT_PRODUCT_DESCRIPTION_CBD, new EditStepMessage(ProductEditStepEnum.description, ADMIN_EDIT_PRODUCT_DESCRIPTION_TEXT)),
            Map.entry(ADMIN_EDIT_PRODUCT_STATUS_CBD, new EditStepMessage(ProductEditStepEnum.status, ADMIN_EDIT_PRODUCT_STATUS_TEXT, ADMIN_EDIT_PRODUCT_STATUS_SET_CBD)),
            Map.entry(ADMIN_EDIT_PRODUCT_PRICE_CBD, new EditStepMessage(ProductEditStepEnum.price, ADMIN_EDIT_PRODUCT_PRICE_TEXT)),
            Map.entry(ADMIN_EDIT_PRODUCT_WEIGHT_CBD, new EditStepMessage(ProductEditStepEnum.weight, ADMIN_EDIT_PRODUCT_WEIGHT_TEXT)),
            Map.entry(ADMIN_EDIT_PRODUCT_CATEGORY_CBD, new EditStepMessage(ProductEditStepEnum.category, ADMIN_EDIT_PRODUCT_CATEGORY_TEXT, ADMIN_EDIT_PRODUCT_CATEGORY_SET_CBD))
    );

    public static final Set<String> editProductCBDSet = Set.of(
            ADMIN_EDIT_PRODUCT_TITLE_CBD,
            ADMIN_EDIT_PRODUCT_DESCRIPTION_CBD,
            ADMIN_EDIT_PRODUCT_PRICE_CBD,
            ADMIN_EDIT_PRODUCT_STATUS_CBD,
            ADMIN_EDIT_PRODUCT_CATEGORY_CBD,
            ADMIN_EDIT_PRODUCT_WEIGHT_CBD
    );
    public static final Set<String> editOrderCBDSet = Set.of(
            ADMIN_ORDER_EDIT_PRICE_CBD,
            ADMIN_ORDER_EDIT_STATUS_CBD,
            ADMIN_ORDER_EDIT_STATUS_SET_CBD
    );


}
