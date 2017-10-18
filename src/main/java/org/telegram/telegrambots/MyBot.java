package org.telegram.telegrambots;

import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(getReceivedMessage(update));
            Object message = new MessageDispatcher().createResponse(update);
            inviaMessaggio(update.getMessage().getChatId(), message);
        }
    }

    private String getReceivedMessage(Update update) {
        return "Message received from "
                + update.getMessage().getChat().getFirstName() + " "
                + update.getMessage().getChat().getLastName() + " : "
                + update.getMessage().getText();
    }

    public void inviaMessaggio(Long chatId, Object message) {
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
            else if(message instanceof SendAudio){
                ((SendAudio) message).setChatId(chatId);
                sendAudio((SendAudio) message);
                System.out.println("Message sent: "+((SendAudio)message).getAudioName());
            }
            else if(message instanceof SendVideo){
                ((SendVideo) message).setChatId(chatId);
                sendVideo((SendVideo) message);
                System.out.println("Message sent: "+((SendVideo)message).getVideoName());
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
            try {
                sendMessage(new SendMessage().setChatId(chatId).setText("Errore durante l'invio del contenuto " + Emoji.ANGRY_FACE));
                System.out.println("Message sent: ERROR");
            } catch (TelegramApiException e1) {
                e1.printStackTrace();
            }
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
