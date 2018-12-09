package com.mytelegrambot.play;

import com.mytelegrambot.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.mytelegrambot.Constants.*;
import static com.mytelegrambot.Helper.getRandomSentence;

@Component
@Log4j2
public class PlayManager {

    private KeyboardManager keyboardManager;
    private MessageManager messageManager;
    private MyBot myBot;

    @Autowired
    public PlayManager(KeyboardManager keyboardManager, MessageManager messageManager, MyBot myBot) {
        this.keyboardManager=keyboardManager;
        this.messageManager=messageManager;
        this.myBot=myBot;
    }

    public String getGame(Long chatId, String sceltaGiocatore) {
        List<String> sceltePossibili = Arrays.asList(SASSO, CARTA, FORBICE);
        String sceltaPc = sceltePossibili.get(new Random().nextInt(sceltePossibili.size()));
        myBot.inviaMessaggio(chatId, messageManager.getSendMessage(sceltaPc));

        if(sceltaGiocatore.equals(sceltaPc)){
            return "PAREGGIO" + Emoji.SMILING_FACE_WITH_OPEN_MOUTH_AND_SMILING_EYES;
        }
        else{
            switch (sceltaGiocatore){
                case SASSO: return sceltaPc.equals(FORBICE) ? "HAI VINTO\n"
                        + getRandomSentence(Constants.RISPOSTE_VITTORIA) : "HAI PERSO\n"
                        + getRandomSentence(Constants.RISPOSTE_SCONFITTA);
                case CARTA: return sceltaPc.equals(SASSO) ? "HAI VINTO\n"
                        + getRandomSentence(Constants.RISPOSTE_VITTORIA) : "HAI PERSO\n"
                        + getRandomSentence(Constants.RISPOSTE_SCONFITTA);
                case FORBICE: return sceltaPc.equals(CARTA) ? "HAI VINTO\n"
                        + getRandomSentence(Constants.RISPOSTE_VITTORIA) : "HAI PERSO\n"
                        + getRandomSentence(Constants.RISPOSTE_SCONFITTA);
            }
        }
        return null;
    }
}
