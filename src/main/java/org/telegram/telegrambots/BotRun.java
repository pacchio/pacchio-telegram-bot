package org.telegram.telegrambots;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class BotRun {

    static final Logger logger = Logger.getLogger(BotRun.class);

    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new MyBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        logger.info("Bot started ...");
    }
}
