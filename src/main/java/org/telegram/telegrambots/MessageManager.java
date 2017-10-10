package org.telegram.telegrambots;

import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;

import java.io.File;
import java.util.Random;

class MessageManager {
    SendMessage createSendMessage(String contenuto){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(contenuto);
        return sendMessage;
    }

    Object getSendPhoto(String folderPath) {
        String photoPath = getRandomPhoto(folderPath);
        if(photoPath != null){
            return new SendPhoto().setNewPhoto(new File(photoPath));
        }
        return new SendMessage().setText("Nessuna foto disponibile ( " + folderPath + " )");
    }

    Object getSendAudio(File audio) {
        return new SendAudio().setNewAudio(audio);
    }

    Object getSendVideo(File video) {
        return new SendVideo().setNewVideo(video);
    }

    private String getRandomPhoto(String folderPath) {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null && listOfFiles.length > 0) {
            int random = new Random().nextInt(listOfFiles.length);
            return listOfFiles[random].getAbsolutePath();
        }
        return null;
    }

}
