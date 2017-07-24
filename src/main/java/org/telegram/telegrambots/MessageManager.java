package org.telegram.telegrambots;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;

class MessageManager {
    
     private KeyboardManager keyboardManager = new KeyboardManager();

     Object createResponse(Update update) {
        SendMessage sendMessage = new SendMessage();
        switch (update.getMessage().getText()){
            case "/start": return keyboardManager.createKeyboardMessage(Constants.INIT_COMMANDS);

            case "Audio":   return keyboardManager.createKeyboardMessage(Constants.AUDIO_COMMANDS);
            case "Foto":    return keyboardManager.createKeyboardMessage(Constants.PHOTO_COMMANDS);
            case "Video":   return keyboardManager.createKeyboardMessage(Constants.VIDEO_COMMANDS);
            case "Messaggiamo":   return keyboardManager.createKeyboardMessage(Constants.TEXT_COMMANDS);

            case "Ciao":            return sendMessage.setText("Ciao Bambolina" + Emoji.FACE_THROWING_A_KISS);
            case "Che ore sono?":   return sendMessage.setText(getTime());
            case "Che giorno è?":   return sendMessage.setText(getDate());
            case "Pajas":           return sendMessage.setText(getTrashSentence());
            case "Ti amo":          return sendMessage.setText("Anche io" + Emoji.SMILING_FACE_WITH_HEART_SHAPED_EYES + Emoji.RED_HEARTH);

            case "Random":  return getSendPhoto(Constants.RANDOM_PHOTOS_FOLDER);
            case "Gatti":   return getSendPhoto(Constants.CAT_PHOTOS_FOLDER);
            case "Meme":    return getSendPhoto(Constants.MEME_PHOTOS_FOLDER);

            case "Indietro": return keyboardManager.createKeyboardMessage(Constants.INIT_COMMANDS);

            default: return sendMessage.setText("Il messaggio '" + update.getMessage().getText() + "' non corrisponde ad alcuna funzione");
        }
    }

    private Object getSendPhoto(String folderPath) {
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

    private String getTrashSentence() {
        return Constants.RISPOSTE_TRASH.get(new Random().nextInt(4));
    }

    private String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar now = Calendar.getInstance();
        return sdf.format(now.getTime());
    }

    private String getDate(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);

        // First convert to Date. This is one of the many ways.
        String dateString = String.format("%d-%d-%d", year, month, day);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then get the day of week from the Date based on specific locale.
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ITALY).format(date);

        return dayOfWeek + ", " + day + "/" + month + "/" + year;
    }
}
