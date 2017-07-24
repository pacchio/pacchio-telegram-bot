package org.telegram.telegrambots;

import static org.telegram.telegrambots.MessageUtils.createResponse;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("Message received: "+update.getMessage().getText());

            Object message = getMessage(update);

            try {
                if(message instanceof SendMessage) {
                    ((SendMessage) message).setChatId(update.getMessage().getChatId());
                    sendMessage((SendMessage) message);
                    System.out.println("Message sent: "+((SendMessage)message).getText());
                }
                else if(message instanceof SendPhoto){
                    ((SendPhoto) message).setChatId(update.getMessage().getChatId());
                    sendPhoto((SendPhoto) message);
                    System.out.println("Message sent: "+((SendPhoto)message).getPhotoName());
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

    private Object getMessage(Update update) {
        return createResponse(update);
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
