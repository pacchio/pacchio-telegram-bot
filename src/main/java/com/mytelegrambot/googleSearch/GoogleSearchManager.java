package com.mytelegrambot.googleSearch;

import com.mytelegrambot.Emoji;
import com.mytelegrambot.KeyboardManager;
import com.mytelegrambot.MessageManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.mytelegrambot.ExceptionHelper.getExceptionStacktrace;

@Component
@Log4j2
public class GoogleSearchManager {

    private KeyboardManager keyboardManager;
    private MessageManager messageManager;

    @Autowired
    public GoogleSearchManager(KeyboardManager keyboardManager, MessageManager messageManager) {
        this.keyboardManager=keyboardManager;
        this.messageManager=messageManager;
    }

    public Object manageResearchOnGoogle(Message message) {
        Object responseDownload = creaResponseRicercaDaGoogle(message);
        if(responseDownload instanceof File) {
            return messageManager.getSendPhoto((File) responseDownload);
        }
        else{
            return messageManager.getSendMessage((String) responseDownload);
        }
    }

    private Object creaResponseRicercaDaGoogle(Message message) {
        String chiaveDiRicerca = sistemaStrinaRicercaGoogle(message);
        GoogleSearchService googleSearchService = new GoogleSearchService();
        DowloadImageManager dowloadImageManager = new DowloadImageManager();
        try {
            List<GResult> results = googleSearchService.search(chiaveDiRicerca);
            Map<Integer, ImageInfo> resultsUrl = googleSearchService.creaListaRisultati(results);
            ImageInfo imageInfo = resultsUrl.get(new Random().nextInt(20));
            return dowloadImageManager.download(imageInfo.getUrl(), imageInfo.getFilename());
        } catch (Exception e) {
            log.error(getExceptionStacktrace(e));
            return e.getMessage() + Emoji.CRYING_CAT_FACE;
        }
    }

    private String sistemaStrinaRicercaGoogle(Message message) {
        return message.getText().substring(1).replaceAll(" ", "%20");
    }
}
