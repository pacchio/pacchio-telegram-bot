package org.telegram.telegrambots;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;

import java.io.File;
import java.util.Random;

class MessageManager {
    SendMessage createSendMessage(String contenuto){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(contenuto);
        return sendMessage;
    }

    public Object getSendPhoto(String folderPath) {
        String photoPath = getRandomPhoto(folderPath);
        if(photoPath != null){
            return new SendPhoto().setNewPhoto(new File(photoPath));
        }
        return new SendMessage().setText("Nessuna foto disponibile");
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
