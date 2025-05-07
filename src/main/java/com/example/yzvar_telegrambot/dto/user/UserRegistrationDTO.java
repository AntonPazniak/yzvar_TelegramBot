package com.example.yzvar_telegrambot.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String phone = "Unknown";

}
