package com.example.yzvar_telegrambot.services.user;


import com.example.yzvar_telegrambot.dto.user.UserDTO;
import com.example.yzvar_telegrambot.entities.user.User;
import com.example.yzvar_telegrambot.enums.UserRoleEnum;
import com.example.yzvar_telegrambot.mapper.UserMapper;
import com.example.yzvar_telegrambot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleUserCache roleCacheService;

    @Override
    public UserDTO createUserIfNotExist(Chat chat) {
        User user = userRepository.findById(chat.getId())
                .orElseGet(() -> {
                    String userName = chat.getUserName() != null ? chat.getUserName() : "Unknown";
                    String firstName = chat.getFirstName() != null ? chat.getFirstName() : "Unknown";
                    String lastName = chat.getLastName() != null ? chat.getLastName() : "Unknown";

                    User newUser = User.builder()
                            .id(chat.getId())
                            .username(userName)
                            .firstName(firstName)
                            .lastName(lastName)
                            .phone("Unknown")
                            .role(roleCacheService.get(UserRoleEnum.USER))
                            .build();

                    return userRepository.save(newUser);
                });

        return UserMapper.toDto(user);
    }

}
