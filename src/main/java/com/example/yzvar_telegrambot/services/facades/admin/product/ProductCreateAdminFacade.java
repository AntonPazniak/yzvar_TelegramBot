package com.example.yzvar_telegrambot.services.facades.admin.product;

import com.example.yzvar_telegrambot.dto.SendMessageDTO;
import com.example.yzvar_telegrambot.dto.product.ProductDTO;
import com.example.yzvar_telegrambot.dto.product.ProductNewStepDTO;
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


    public SendMessageDTO createProduct(Long idUser, String text) {
        if (!newProductMap.containsKey(idUser)) {
            return createProductStart(idUser);
        }

        if (text.equalsIgnoreCase(ADMIN_NEW_PRODUCT_CANCEL_CBD)) {
            return cancelCreateProduct(idUser);
        }

        var dto = newProductMap.get(idUser);
        ProductStepEnum step = dto.getProductStep();

        return switch (step) {
            case title -> createProductSetTitle(dto, text);
            case description -> createProductSetDescription(dto, text);
            case category -> createProductSetCategory(dto, CategoryEnum.valueOf(text));
            case price -> createProductSetPrice(dto, text);
            case weight -> createProductSetWeightAndSave(dto, text);
        };
    }


    private SendMessageDTO cancelCreateProduct(Long idUser) {
        newProductMap.remove(idUser);
        return SendMessageDTO.builder()
                .messages(List.of(new SendMessage(idUser.toString(), ADMIN_NEW_PRODUCT_CANCEL_TEXT)))
                .status(false)
                .build();
    }

    private SendMessageDTO createProductStart(Long idUser) {
        var dto = SendMessageDTO.builder().build();
        if (userService.isUserAdminById(idUser)) {
            var message = new SendMessage(idUser.toString(), ADMIN_NEW_PRODUCT_TITLE_TEXT);
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

    private SendMessageDTO createProductSetTitle(ProductNewStepDTO productStepDTO, String title) {
        productStepDTO.getProductDTO().setTitle(title);
        productStepDTO.setProductStep(ProductStepEnum.description);
        var message = new SendMessage(productStepDTO.getChatId().toString(), ADMIN_NEW_PRODUCT_DESCRIPTION_TEXT);
        message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());

        return SendMessageDTO.builder()
                .status(true)
                .messages(List.of(message))
                .build();
    }

    private SendMessageDTO createProductSetDescription(ProductNewStepDTO productStepDTO, String description) {
        productStepDTO.getProductDTO().setDescription(description);
        productStepDTO.setProductStep(ProductStepEnum.category);
        var message = new SendMessage(productStepDTO.getChatId().toString(), ADMIN_NEW_PRODUCT_CATEGORY_TEXT);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (CategoryEnum category : CategoryEnum.values()) {
            var button = new InlineKeyboardButton(category.name());
            button.setCallbackData(ADMIN_NEW_PRODUCT_CATEGORY_CBD + ":" + category.name());
            rows.add(List.of(button));
        }

        message.setReplyMarkup(new InlineKeyboardMarkup(rows));

        return new SendMessageDTO(true, List.of(message));
    }

    private SendMessageDTO createProductSetCategory(ProductNewStepDTO productStepDTO, CategoryEnum category) {
        switch (category) {
            case MEAT -> productStepDTO.getProductDTO().setCategory(CategoryEnum.MEAT);
            case VEGETARIAN -> productStepDTO.getProductDTO().setCategory(CategoryEnum.VEGETARIAN);
        }
        productStepDTO.setProductStep(ProductStepEnum.price);
        var message = new SendMessage(productStepDTO.getChatId().toString(), ADMIN_NEW_PRODUCT_PRICE_TEXT);
        message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());
        return new SendMessageDTO(true, List.of(message));
    }

    private SendMessageDTO createProductSetPrice(ProductNewStepDTO productStepDTO, String priceString) {
        try {
            Float price = Float.parseFloat(priceString);
            productStepDTO.getProductDTO().setPrice(price);
            productStepDTO.setProductStep(ProductStepEnum.weight);

            var message = new SendMessage(productStepDTO.getChatId().toString(), ADMIN_NEW_PRODUCT_WEIGHT_TEXT);
            message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());

            return SendMessageDTO.builder()
                    .status(true)
                    .messages(List.of(message))
                    .build();

        } catch (NumberFormatException e) {

            var message = new SendMessage(productStepDTO.getChatId().toString(), ADMIN_NEW_PRODUCT_PRICE_ERROR_TEXT);
            message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());

            return SendMessageDTO.builder()
                    .status(true)
                    .messages(List.of(message))
                    .build();
        }
    }

    private SendMessageDTO createProductSetWeightAndSave(ProductNewStepDTO productStepDTO, String weightString) {
        try {
            Integer weight = Integer.parseInt(weightString);
            productStepDTO.getProductDTO().setWeight(weight);

            var dto = productService.createProduct(productStepDTO.getProductDTO());

            newProductMap.remove(productStepDTO.getChatId());


            var message1 = new SendMessage(productStepDTO.getChatId().toString(), ADMIN_NEW_PRODUCT_SUCCESS_TEXT);
            var message2 = new SendMessage(productStepDTO.getChatId().toString(), dto.toString());

            return SendMessageDTO.builder()
                    .status(false)
                    .messages(List.of(message1, message2))
                    .build();

        } catch (NumberFormatException e) {

            var message = new SendMessage(productStepDTO.getChatId().toString(), ADMIN_NEW_PRODUCT_WEIGHT_ERROR_TEXT);
            message.setReplyMarkup(getInlineKeyboardMarkupWithCancelButton());

            return SendMessageDTO.builder()
                    .status(true)
                    .messages(List.of(message))
                    .build();
        }
    }


}
