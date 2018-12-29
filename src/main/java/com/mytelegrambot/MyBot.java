package com.mytelegrambot;

import com.mytelegrambot.raspberry.RaspberryManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private RaspberryManager raspberryManager;

    @Scheduled(cron = "0 0/6 * * * ?")
    public void scheduleTaskWithCronExpression() {
        try {
            if(raspberryManager.getSchedulingState()) {
                inviaMessaggio(342647616L, raspberryManager.testMoneyHoney());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        taskExecutor.execute(() -> elabora(update));
    }

    private void elabora(Update update) {
        if (textMessage(update) || contactMessage(update)) {
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
                + getContentMessage(update);
    }

    private String getContentMessage(Update update) {
        if(contactMessage(update)) {
            return update.getMessage().getContact().getPhoneNumber();
        } else {
            return update.getMessage().getText();
        }
    }

    private boolean contactMessage(Update update) {
        return update.getMessage().getContact() != null;
    }

    private boolean textMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
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
