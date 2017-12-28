package com.mytelegrambot;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import static com.mytelegrambot.ExceptionHelper.getExceptionStacktrace;

@Log4j2
@Component
public class MyBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Autowired
    private MessageDispatcher messageDispatcher;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            log.info(getReceivedMessage(update));
            Object message = messageDispatcher.createResponse(update);
            inviaMessaggio(update.getMessage().getChatId(), message);
        }
    }

    public void inviaMessaggio(Long chatId, Object message) {
        try {
            if(message instanceof SendMessage) {
                ((SendMessage) message).setChatId(chatId);
                execute((SendMessage) message);
                log.info("Message sent: "+((SendMessage)message).getText());
            }
            else if(message instanceof SendPhoto){
                ((SendPhoto) message).setChatId(chatId);
                sendPhoto((SendPhoto) message);
                log.info("Message sent: "+((SendPhoto)message).getPhotoName());
            }
            else if(message instanceof SendAudio){
                ((SendAudio) message).setChatId(chatId);
                sendAudio((SendAudio) message);
                log.info("Message sent: "+((SendAudio)message).getAudioName());
            }
            else if(message instanceof SendVideo){
                ((SendVideo) message).setChatId(chatId);
                sendVideo((SendVideo) message);
                log.info("Message sent: "+((SendVideo)message).getVideoName());
            }
        }
        catch (TelegramApiException e) {
            log.error(getExceptionStacktrace(e));
            try {
                execute(new SendMessage().setChatId(chatId).setText("Errore durante l'invio del contenuto " + Emoji.ANGRY_FACE));
                log.error("Message sent: ERROR");
            } catch (TelegramApiException e1) {
                log.error(getExceptionStacktrace(e));
            }
        }
    }

    private String getReceivedMessage(Update update) {
        return "Message received from "
                + update.getMessage().getChat().getFirstName() + " "
                + update.getMessage().getChat().getLastName() + " : "
                + update.getMessage().getText();
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
