package org.telegram.telegrambots;

import static org.telegram.telegrambots.KeyboardUtils.createKeyboardMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

class MessageUtils {

    static SendMessage createResponse(Update update) {
        SendMessage sendMessage = new SendMessage();
        switch (update.getMessage().getText()){
            case "\\start": return createKeyboardMessage(Constants.INIT_COMMANDS);
            case "Audio":   return createKeyboardMessage(Constants.AUDIO_COMMANDS);
            case "Foto":    return createKeyboardMessage(Constants.PHOTO_COMMANDS);
            case "Video":   return createKeyboardMessage(Constants.VIDEO_COMMANDS);
            case "Testo":   return createKeyboardMessage(Constants.TEXT_COMMANDS);
            case "Ciao":    return sendMessage.setText("Ciao Pacchio");
            case "Ora":     return sendMessage.setText(getTime());
            case "Data":    return sendMessage.setText(getDate());

//            case "Random":   return new SendPhoto().setPhoto("D:\\Users\\arx50054\\Desktop\\meme1.jpeg");

            case "Indietro": return createKeyboardMessage(Constants.INIT_COMMANDS);

            default: return sendMessage.setText("Il messaggio '" + update.getMessage().getText() + "' non corrisponde ad alcuna funzione");
        }
    }

    private static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar now = Calendar.getInstance();
        return sdf.format(now.getTime());
    }

    private static String getDate(){
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
