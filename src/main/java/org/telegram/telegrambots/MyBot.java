package org.telegram.telegrambots;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("Message received: "+update.getMessage().getText());
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());

            if(update.getMessage().getText().equals("\\start")){
                message.setReplyMarkup(getKeyboard(Constants.INIT_COMMANDS));
            }

            if(update.getMessage().getText().equals("Audio")){
                message.setReplyMarkup(getKeyboard(Constants.AUDIO_COMMANDS));
            }
            if(update.getMessage().getText().equals("Foto")){
                message.setReplyMarkup(getKeyboard(Constants.PHOTO_COMMANDS));
            }
            if(update.getMessage().getText().equals("Video")){
                message.setReplyMarkup(getKeyboard(Constants.VIDEO_COMMANDS));
            }
            if(update.getMessage().getText().equals("Testo")){
                message.setReplyMarkup(getKeyboard(Constants.TEXT_COMMANDS));
            }
            message.setText("Scegli un opzione");

            try {
                sendMessage(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            System.out.println("Message sent: "+message.getText());

        }
    }

    private ReplyKeyboardMarkup getKeyboard(List<String> commands) {
        List<List<String>> keyboardCommands = new ArrayList<>();

        for(int i=0, j=0; i<commands.size()/2; i++){
            keyboardCommands.add(getCommandRow(commands.get(j), commands.get(j+1)));
            j+=2;
        }

        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        replyKeyboard.setKeyboard(getKeyboardRows(keyboardCommands));
        return replyKeyboard;
    }

    private List<KeyboardRow> getKeyboardRows(List<List<String>> commands) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for(List<String> commandsRow : commands) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (String command : commandsRow) {
                KeyboardButton keyboardButton = new KeyboardButton();
                keyboardButton.setText(command);
                keyboardRow.add(keyboardButton);
            }
            keyboardRows.add(keyboardRow);
        }
        return keyboardRows;
    }

    private List<String> getCommandRow(String command1, String command2) {
        List<String> commandsRow = new ArrayList<>();
        commandsRow.add(command1);
        commandsRow.add(command2);
        return commandsRow;
    }

    private String createResponse(Update update) {
        String response = "Il messaggio '" + update.getMessage().getText() + "' non corrisponde a nessuna funzione disponibile: ";
        switch (update.getMessage().getText()){
            case "Ciao": response = "Ciao Pacchio"; break;
            case "Ora": response = getTime(); break;
            case "Data": response = getDate(); break;
            case "Amore": response = "Ciao Marti " + "\u2764"; break;
        }
        return response;
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
