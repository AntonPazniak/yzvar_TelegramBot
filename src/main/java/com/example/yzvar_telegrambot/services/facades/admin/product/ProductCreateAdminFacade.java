package com.example.yzvar_telegrambot.services.facades.admin.product;

import com.example.yzvar_telegrambot.dto.product.ProductDTO;
import com.example.yzvar_telegrambot.dto.product.ProductNewStepDTO;
import com.example.yzvar_telegrambot.dto.product.SendMessageNewProductDTO;
import com.example.yzvar_telegrambot.enums.CategoryEnum;
import com.example.yzvar_telegrambot.enums.ProductStepEnum;
import com.example.yzvar_telegrambot.services.product.ProductService;
import com.example.yzvar_telegrambot.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.yzvar_telegrambot.configurations.ClientBotConfig.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCreateAdminFacade {

    private final UserService userService;
    private final ProductService productService;
    private final Map<Long, ProductNewStepDTO> newProductMap = new HashMap<>();
    private final Map<Long, ProductNewStepDTO> editProductMap = new HashMap<>();


    public SendMessageNewProductDTO createProduct(Long idUser, String text) {
        if (!newProductMap.containsKey(idUser)) {
            return createProductStart(idUser);
        }

        if (text.equalsIgnoreCase(NEW_PRODUCT_CANCEL_COMMAND)) {
            return cancelCreateProduct(idUser);
        }

        var dto = newProductMap.get(idUser);
        ProductStepEnum step = dto.getProductStep();

        return switch (step) {
            case title -> createProductSetTitle(dto, text);
            case description -> createProductSetDescription(dto, text);
            case category -> createProductSetCategory(dto, text);
            case price -> createProductSetPrice(dto, text);
            case weight -> createProductSetWeightAndSave(dto, text);
        };
    }


    private SendMessageNewProductDTO cancelCreateProduct(Long idUser) {
        newProductMap.remove(idUser);
        return SendMessageNewProductDTO.builder()
                .messages(List.of(new SendMessage(idUser.toString(), NEW_PRODUCT_CANCEL_TEXT)))
                .status(false)
                .build();
    }

    private SendMessageNewProductDTO createProductStart(Long idUser) {
        var dto = SendMessageNewProductDTO.builder().build();
        if (userService.isUserAdminById(idUser)) {
            var message = new SendMessage(idUser.toString(), NEW_PRODUCT_TITLE_TEXT);
            message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());
            newProductMap.put(idUser,
                    ProductNewStepDTO.builder()
                            .chatId(idUser)
                            .productStep(ProductStepEnum.title)
                            .productDTO(ProductDTO.builder().build())
                            .build());
            dto.setMessages(List.of(message));
            dto.setStatus(true);
        } else {
            dto.setStatus(false);
            dto.setMessages(List.of());
        }

        return dto;
    }

    private SendMessageNewProductDTO createProductSetTitle(ProductNewStepDTO productStepDTO, String title) {
        productStepDTO.getProductDTO().setTitle(title);
        productStepDTO.setProductStep(ProductStepEnum.description);
        var message = new SendMessage(productStepDTO.getChatId().toString(), NEW_PRODUCT_DESCRIPTION_TEXT);
        message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());

        return SendMessageNewProductDTO.builder()
                .status(true)
                .messages(List.of(message))
                .build();
    }

    private SendMessageNewProductDTO createProductSetDescription(ProductNewStepDTO productStepDTO, String description) {
        productStepDTO.getProductDTO().setDescription(description);
        productStepDTO.setProductStep(ProductStepEnum.category);
        var message = new SendMessage(productStepDTO.getChatId().toString(), NEW_PRODUCT_CATEGORY_TEXT);

        var meatButton = new InlineKeyboardButton();
        meatButton.setText(NEW_PRODUCT_CATEGORY_MEAT_TEXT_BUTTON);
        meatButton.setCallbackData(NEW_PRODUCT_CATEGORY_MEAT_COMMAND);

        var vegetarianButton = new InlineKeyboardButton();
        vegetarianButton.setText(NEW_PRODUCT_CATEGORY_VEGETARIAN_TEXT_BUTTON);
        vegetarianButton.setCallbackData(NEW_PRODUCT_CATEGORY_VEGETARIAN_COMMAND);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();

        rowInLine.add(vegetarianButton);
        rowInLine.add(meatButton);
        rowsInLine.add(rowInLine);

        rowInLine2.add(getCancelButton());
        rowsInLine.add(rowInLine2);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        return SendMessageNewProductDTO.builder()
                .status(true)
                .messages(List.of(message))
                .build();
    }

    private SendMessageNewProductDTO createProductSetCategory(ProductNewStepDTO productStepDTO, String category) {
        if (category.equals(NEW_PRODUCT_CATEGORY_MEAT_COMMAND)) {
            productStepDTO.getProductDTO().setCategory(CategoryEnum.MEAT);
        } else if (category.equals(NEW_PRODUCT_CATEGORY_VEGETARIAN_COMMAND)) {
            productStepDTO.getProductDTO().setCategory(CategoryEnum.VEGETARIAN);
        }
        productStepDTO.setProductStep(ProductStepEnum.price);

        var message = new SendMessage(productStepDTO.getChatId().toString(), NEW_PRODUCT_PRICE_TEXT);
        message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());

        return SendMessageNewProductDTO.builder()
                .status(true)
                .messages(List.of(message))
                .build();
    }

    private SendMessageNewProductDTO createProductSetPrice(ProductNewStepDTO productStepDTO, String priceString) {
        try {
            Float price = Float.parseFloat(priceString);
            productStepDTO.getProductDTO().setPrice(price);
            productStepDTO.setProductStep(ProductStepEnum.weight);

            var message = new SendMessage(productStepDTO.getChatId().toString(), NEW_PRODUCT_WEIGHT_TEXT);
            message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());

            return SendMessageNewProductDTO.builder()
                    .status(true)
                    .messages(List.of(message))
                    .build();

        } catch (NumberFormatException e) {

            var message = new SendMessage(productStepDTO.getChatId().toString(), NEW_PRODUCT_PRICE_ERROR_TEXT);
            message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());

            return SendMessageNewProductDTO.builder()
                    .status(true)
                    .messages(List.of(message))
                    .build();
        }
    }

    private SendMessageNewProductDTO createProductSetWeightAndSave(ProductNewStepDTO productStepDTO, String weightString) {
        try {
            Integer weight = Integer.parseInt(weightString);
            productStepDTO.getProductDTO().setWeight(weight);

            var dto = productService.createProduct(productStepDTO.getProductDTO());

            newProductMap.remove(productStepDTO.getChatId());


            var message1 = new SendMessage(productStepDTO.getChatId().toString(), NEW_PRODUCT_SUCCESS_TEXT);
            var message2 = new SendMessage(productStepDTO.getChatId().toString(), dto.toString());

            return SendMessageNewProductDTO.builder()
                    .status(false)
                    .messages(List.of(message1, message2))
                    .build();

        } catch (NumberFormatException e) {

            var message = new SendMessage(productStepDTO.getChatId().toString(), NEW_PRODUCT_WEIGHT_ERROR_TEXT);
            message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());

            return SendMessageNewProductDTO.builder()
                    .status(true)
                    .messages(List.of(message))
                    .build();
        }
    }


}
