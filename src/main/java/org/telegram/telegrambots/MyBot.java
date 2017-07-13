package org.telegram.telegrambots;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = createResponse(update);
            System.out.println("Message received: "+update.getMessage().getText());
            try {
                sendMessage(message);
                System.out.println("Message sent: "+message.getText());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage createResponse(Update update) {
        String response = "Il messaggio inviato non corrisponde a nessuna funzione disponibile: "+update.getMessage().getText();
        switch (update.getMessage().getText()){
            case "Ciao": response = "Ciao Pacchio"; break;
            case "Ora": response = getTime(); break;
            case "Data": response = getDate(); break;
            case "Amore": response = "Ciao Marti " + "\u2764"; break;
//            case "Img": return new SendMessage();
        }
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(response);
    }

    private String getTime(){
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);

        return hour + ":" + minute + ":" + second;
    }

    private String getDate(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);

        // First convert to Date. This is one of the many ways.
        String dateString = String.format("%d-%d-%d", year, month, day);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then get the day of week from the Date based on specific locale.
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ITALY).format(date);

        return dayOfWeek + ", " + day + "/" + month + "/" + year;
    }

    @Override
    public String getBotUsername() {
        return "pacchio97_bot";
    }

    @Override
    public String getBotToken() {
        return "377464414:AAEq-w7-D89rKdXjFjildYgBn2KwvuCGVrc";
    }
}
