package com.example.yzvar_telegrambot.services.facades.user.product;

import com.example.yzvar_telegrambot.configurations.ClientBotConfig;
import com.example.yzvar_telegrambot.mapper.ProductMapper;
import com.example.yzvar_telegrambot.services.product.ProductCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductCache productCache;


    public List<SendMessage> getAllProducts(Long chatId) {
        var products = productCache.getAllActiveProducts();
        var massages = new ArrayList<SendMessage>();
        products.forEach(
                product -> {
                    var message = new SendMessage(chatId.toString(), ProductMapper.toDto(product).toString());
                    InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
                    List<InlineKeyboardButton> rowInLine = new ArrayList<>();
                    var orderButton = new InlineKeyboardButton();

                    orderButton.setText(ClientBotConfig.ORDER_BUTTON_TEXT);
                    orderButton.setCallbackData(ClientBotConfig.NEW_ORDER_CBD + ":" + product.getId());

                    rowInLine.add(orderButton);

                    rowsInLine.add(rowInLine);

                    markupInLine.setKeyboard(rowsInLine);
                    message.setReplyMarkup(markupInLine);
                    massages.add(message);
                }
        );
        return massages;
    }


}
