package com.mytelegrambot.youtubeSearch;

import com.google.api.services.youtube.model.SearchListResponse;
import com.mytelegrambot.Emoji;
import com.mytelegrambot.KeyboardManager;
import com.mytelegrambot.MessageManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import java.io.IOException;

import static com.mytelegrambot.ExceptionHelper.getExceptionStacktrace;

@Component
@Log4j2
public class YoutubeSearchManager {

    private KeyboardManager keyboardManager;
    private MessageManager messageManager;

    @Autowired
    public YoutubeSearchManager(KeyboardManager keyboardManager, MessageManager messageManager) {
        this.keyboardManager=keyboardManager;
        this.messageManager=messageManager;
    }

    public SendMessage youtubeManager(Message message) {
        return messageManager.getSendMessage(creaResponseRicercaDaYoutube(message.getText().substring(1)));
    }

    private String creaResponseRicercaDaYoutube(String chiaveDiRicerca) {
        YoutubeService youtubeService = new YoutubeService();
        try {
            SearchListResponse response = youtubeService.ricercaSuYoutube(chiaveDiRicerca);
            return youtubeService.visualizzaListaRisultati(response);
        } catch (IOException e) {
            log.error(getExceptionStacktrace(e));
            return "Errore durante la ricerca " + Emoji.CRYING_CAT_FACE;
        }
    }
}
