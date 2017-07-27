package org.telegram.telegrambots;

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
            Object message = new MessageDispatcher().createResponse(update);
            inviaMessaggio(update.getMessage().getChatId(), message);
        }
    }

    void inviaMessaggio(Long chatId, Object message) {
        try {
            if(message instanceof SendMessage) {
                ((SendMessage) message).setChatId(chatId);
                sendMessage((SendMessage) message);
                System.out.println("Message sent: "+((SendMessage)message).getText());
            }
            else if(message instanceof SendPhoto){
                ((SendPhoto) message).setChatId(chatId);
                sendPhoto((SendPhoto) message);
                System.out.println("Message sent: "+((SendPhoto)message).getPhotoName());
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
