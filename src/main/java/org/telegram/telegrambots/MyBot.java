package org.telegram.telegrambots;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.StrictMath.toIntExact;

public class MyBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("Message received: "+update.getMessage().getText());
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());

            if(update.getMessage().getText().equals("\\start")){
                message.setText("");
                message.setReplyMarkup(getKeyboard());
            }
            else {
                message.setText(createResponse(update));
            }

            try {
                sendMessage(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            System.out.println("Message sent: "+message.getText());

        }
    }

    private ReplyKeyboardMarkup getKeyboard() {
        List<List<String>> commands = new ArrayList<>();

        commands.add(getCommandRow("Ciao", "Ora"));
        commands.add(getCommandRow("Data", "Foto"));

        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
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
        replyKeyboard.setKeyboard(keyboardRows);
        return replyKeyboard;
    }

    private List<String> getCommandRow(String command1, String command2) {
        List<String> commandsRow = new ArrayList<>();
        commandsRow.add(command1);
        commandsRow.add(command2);
        return commandsRow;
    }

    private String createResponse(Update update) {
        String response = "Il messaggio inviato non corrisponde a nessuna funzione disponibile: "+update.getMessage().getText();
        switch (update.getMessage().getText()){
            case "Ciao": response = "Ciao Pacchio"; break;
            case "Ora": response = getTime(); break;
            case "Data": response = getDate(); break;
            case "Amore": response = "Ciao Marti " + "\u2764"; break;
//            case "Img": return new SendMessage();
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
