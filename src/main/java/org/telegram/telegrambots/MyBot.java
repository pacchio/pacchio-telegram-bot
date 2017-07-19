package org.telegram.telegrambots;

import static org.telegram.telegrambots.MessageUtils.createResponse;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("Message received: "+update.getMessage().getText());

            SendMessage message = getSendMessage(update);

            try {
                sendMessage(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            System.out.println("Message sent: "+message.getText());

        }
    }

    private SendMessage getSendMessage(Update update) {
        SendMessage message = createResponse(update);
        message.setChatId(update.getMessage().getChatId());
        return message;
    }

    @Override
    public String getBotUsername() {
        return "pacchio97_bot";
    }

    @Override
    public String getBotToken() {
        return "377464414:AAEq-w7-D89rKdXjFjildYgBn2KwvuCGVrc";
    }
}
