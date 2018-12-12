package com.mytelegrambot.raspberry;

import com.mytelegrambot.Constants;
import com.mytelegrambot.KeyboardManager;
import com.mytelegrambot.MessageManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mytelegrambot.ExceptionHelper.getExceptionStacktrace;

@Component
@Log4j2
public class RaspberryManager {

    private KeyboardManager keyboardManager;
    private MessageManager messageManager;

    private String rasperryToken = "";
    private boolean schedulingState = false;

    @Autowired
    public RaspberryManager(KeyboardManager keyboardManager, MessageManager messageManager) {
        this.keyboardManager=keyboardManager;
        this.messageManager=messageManager;
    }

    public String getRasperryToken() {
        return rasperryToken;
    }

    public boolean getSchedulingState() {
        return schedulingState;
    }

    public SendMessage manageRaspberryAuthentication(Message message) {
        if(StringUtils.equals(message.getText().substring(1), Constants.RASPBERRY_PWD)){
            rasperryToken = (new Random().nextInt((999 - 100) + 1) + 100) + "";
            return keyboardManager.createKeyboardMessage(raspberryCommandsWithToken());
        } else {
            return messageManager.getSendMessage("Password errata.");
        }
    }

    public SendMessage manageRaspberry(Message message) {
        if(message.getText().contains("MoneyHoney")){
            try {
                return testMoneyHoney();
            } catch (Exception e) {
                log.error(getExceptionStacktrace(e));
                return messageManager.getSendMessage("Errore durante l'esecuzione del comando");
            }
        }
        if(message.getText().contains("Toggle scheduling")){
            try {
                schedulingState = !schedulingState;
                return messageManager.getSendMessage("Stato scheduling: " + schedulingState);
            } catch (Exception e) {
                log.error(getExceptionStacktrace(e));
                return messageManager.getSendMessage("Errore durante l'esecuzione del comando");
            }
        }
        return getDefaultMessage(message);
    }

    public SendMessage testMoneyHoney() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("taskkill /IM cmd.exe");
        Thread.sleep(500);
        Runtime.getRuntime().exec("cmd /c start \"\" " + Constants.MONEYHONEY_PATH + "launch.bat");
        return messageManager.getSendMessage("Comando eseguito correttamente");
    }

    private SendMessage getDefaultMessage(Message message) {
        return messageManager.getSendMessage("Il messaggio '" + message.getText() + "' non corrisponde ad alcuna funzione");
    }

    private List<String> raspberryCommandsWithToken() {
        List<String> commands = new ArrayList<>();
        for(String command : Constants.RASPBERRY_COMMANDS) {
            if(!StringUtils.equals(command, "Indietro")) {
                commands.add("[" + rasperryToken + "] " + command);
            } else {
                commands.add(command);
            }
        }
        return commands;
    }
}
