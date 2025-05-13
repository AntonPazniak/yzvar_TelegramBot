package com.example.yzvar_telegrambot.services.facades.admin.product;

import com.example.yzvar_telegrambot.dto.SendMessageDTO;
import com.example.yzvar_telegrambot.dto.product.ProductDTO;
import com.example.yzvar_telegrambot.dto.product.ProductEditStepDTO;
import com.example.yzvar_telegrambot.entities.product.Product;
import com.example.yzvar_telegrambot.enums.ProductEditStepEnum;
import com.example.yzvar_telegrambot.mapper.ProductMapper;
import com.example.yzvar_telegrambot.services.product.CategoryProductCache;
import com.example.yzvar_telegrambot.services.product.ProductCache;
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
public class ProductEditAdminFacade {

    private final ProductService productService;
    private final ProductCache productCache;
    private final CategoryProductCache categoryCache;
    private final UserService userService;
    private final Map<Long, ProductEditStepDTO> productMap = new HashMap<>();


    // 0 = all
    // 1 = active
    // 2 = not active
    public List<SendMessage> getProductsForAdminByStatus(Long chatId, int status) {
        if (userService.isUserAdminById(chatId)) {
            var products = productCache.getAll();
            if (status == 1) {
                products = products.stream().filter(Product::getActive).toList();
            } else if (status == 2) {
                products = products.stream().filter(product -> !product.getActive()).toList();
            }
            var productsDTO = products.stream().map(ProductMapper::toDto).toList();

            return productsDTO.stream()
                    .map(dto -> {
                        var message = SendMessage.builder()
                                .chatId(chatId)
                                .text(dto.toString())
                                .build();
                        message.setReplyMarkup(buildEditButtonMarkup(dto.getId()));
                        return message;
                    }).toList();
        }
        return List.of();
    }

    public SendMessageDTO startProductEditStep(Long chatId, String command, Long productId) {
        if (userService.isUserAdminById(chatId)) {
            var messageConfig = editStepMessageMap.get(command);
            var step = messageConfig.getProductEditStep();
            var dto = ProductEditStepDTO.builder()
                    .productDTO(ProductMapper.toDto(productCache.getById(productId)))
                    .productEditStep(step)
                    .build();
            productMap.put(chatId, dto);

            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            switch (step) {
                case category -> {
                    var categories = categoryCache.getAll();
                    for (var category : categories) {
                        var button = new InlineKeyboardButton(category.getName().name());
                        button.setCallbackData(messageConfig.getCollBackData() + ":" + category.getId());
                        rows.add(List.of(button));
                    }
                }
                case status -> {
                    var trueButton = new InlineKeyboardButton(EDIT_PRODUCT_STATUS_VISIBLE_TEXT_BUTTON);
                    var falseButton = new InlineKeyboardButton(EDIT_PRODUCT_STATUS_INVISIBLE_TEXT_BUTTON);
                    trueButton.setCallbackData(messageConfig.getCollBackData() + ":true");
                    falseButton.setCallbackData(messageConfig.getCollBackData() + ":false");
                    rows.add(List.of(trueButton));
                    rows.add(List.of(falseButton));
                }
            }

            var message = new SendMessage(chatId.toString(), messageConfig.getMessage());
            message.setReplyMarkup(new InlineKeyboardMarkup(rows));
            return SendMessageDTO.builder()
                    .messages(List.of(message))
                    .status(true)
                    .build();
        }
        return new SendMessageDTO();
    }

    public List<SendMessage> showProductEditOptions(Long chatId, Long productId) {
        if (userService.isUserAdminById(chatId)) {
            var dto = ProductMapper.toDto(productCache.getById(productId));

            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            for (List<String> list : callbackList) {
                var button = new InlineKeyboardButton(list.get(1));
                button.setCallbackData(list.get(0) + ":" + productId);
                rows.add(List.of(button));
            }
            var keyboard = new InlineKeyboardMarkup(rows);
            var message = new SendMessage(chatId.toString(), dto.toString());

            message.setReplyMarkup(keyboard);

            return List.of(message);
        }
        return List.of();
    }

    public SendMessageDTO applyProductEditAndSave(Long chatId, String text) {
        if (!userService.isUserAdminById(chatId)) {
            return new SendMessageDTO();
        }

        var dto = productMap.get(chatId);
        var step = dto.getProductEditStep();
        var productDTO = dto.getProductDTO();

        try {
            applyStepEdit(productDTO, step, text);
        } catch (IllegalArgumentException e) {
            return errorResponse(chatId, e.getMessage());
        }

        var updatedProduct = productService.updateProduct(productDTO);
        productMap.remove(chatId);

        return successResponse(chatId, updatedProduct.toString());
    }


    private void applyStepEdit(ProductDTO product, ProductEditStepEnum step, String text) {
        switch (step) {
            case title -> product.setTitle(text);
            case description -> product.setDescription(text);
            case category -> product.setCategory(categoryCache.getCategoryEnumById(Long.parseLong(text)));
            case status -> product.setStatus(Boolean.parseBoolean(text));
            case price -> {
                try {
                    product.setPrice(Float.parseFloat(text));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(ADMIN_EDIT_PRODUCT_PRICE_ERROR_TEXT);
                }
            }
            case weight -> {
                try {
                    product.setWeight(Integer.parseInt(text));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(ADMIN_EDIT_PRODUCT_WEIGHT_TEXT);
                }
            }
        }
    }

    private SendMessageDTO successResponse(Long chatId, String text) {
        return SendMessageDTO.builder()
                .messages(List.of(new SendMessage(chatId.toString(), text)))
                .status(true)
                .build();
    }

    private SendMessageDTO errorResponse(Long chatId, String errorText) {
        return SendMessageDTO.builder()
                .messages(List.of(new SendMessage(chatId.toString(), errorText)))
                .status(true)
                .build();
    }


    private InlineKeyboardMarkup buildEditButtonMarkup(Long idProduct) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var editProduct = new InlineKeyboardButton();
        editProduct.setText(EDIT_TEXT_BUTTON);
        editProduct.setCallbackData(ADMIN_EDIT_PRODUCT_CBD + ":" + idProduct);

        rowInLine.add(editProduct);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;

    }

}
