package org.telegram.telegrambots;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

class KeyboardUtils {

    static SendMessage createKeyboardMessage(List<String> commands){
        SendMessage sendMessage = new SendMessage();

        sendMessage.setReplyMarkup(getKeyboard(commands));
        sendMessage.setText("Scegli un'opzione");

        return sendMessage;
    }

    private static ReplyKeyboardMarkup getKeyboard(List<String> commands) {
        List<List<String>> keyboardCommands = new ArrayList<>();

        for(int i=0, j=0; i<commands.size()/2; i++){
            keyboardCommands.add(getCommandRow(commands.get(j), commands.get(j+1)));
            j+=2;
        }

        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        replyKeyboard.setKeyboard(getKeyboardRows(keyboardCommands));
        return replyKeyboard;
    }

    private static List<KeyboardRow> getKeyboardRows(List<List<String>> commands) {
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

    private static List<String> getCommandRow(String command1, String command2) {
        List<String> commandsRow = new ArrayList<>();
        commandsRow.add(command1);
        commandsRow.add(command2);
        return commandsRow;
    }

}
