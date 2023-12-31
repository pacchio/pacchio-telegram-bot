package com.mytelegrambot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;

import java.io.File;

@Component
public class MessageManager {

    public SendMessage getSendMessage(String contenuto){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(contenuto);
        return sendMessage;
    }

    public SendPhoto getSendPhoto(File photo) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setNewPhoto(photo);
        return sendPhoto;
    }

    SendAudio getSendAudio(File audio) {
        return new SendAudio().setNewAudio(audio);
    }

    SendVideo getSendVideo(File video) {
        return new SendVideo().setNewVideo(video);
    }

}
