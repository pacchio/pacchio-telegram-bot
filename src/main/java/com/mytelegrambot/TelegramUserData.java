package com.mytelegrambot;

import lombok.Data;

@Data
public class TelegramUserData {

    Long chatID;
    String firstname;
    String lastname;
    String username;
    String telephoneNumber;

}
