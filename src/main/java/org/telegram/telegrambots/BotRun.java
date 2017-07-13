package org.telegram.telegrambots;

import org.telegram.telegrambots.exceptions.TelegramApiException;

public class BotRun {
    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new MyBot());
            System.out.println("Bot started ...");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
