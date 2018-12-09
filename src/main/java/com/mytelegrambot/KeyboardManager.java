package com.mytelegrambot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardManager {

    SendMessage createKeyboardContact(List<String> commands){
        SendMessage sendMessage = new SendMessage();

        sendMessage.setReplyMarkup(getKeyboard(commands, true));
        sendMessage.setText("Condividi il tuo numero per proseguire" + Emoji.FACE_THROWING_A_KISS);

        return sendMessage;
    }

    public SendMessage createKeyboardMessage(List<String> commands){
        SendMessage sendMessage = new SendMessage();

        sendMessage.setReplyMarkup(getKeyboard(commands, false));
        sendMessage.setText("Scegli un'opzione");

        return sendMessage;
    }

    private ReplyKeyboardMarkup getKeyboard(List<String> commands, boolean contact) {
        List<List<String>> keyboardCommands = new ArrayList<>();
        int j=0;
        for(int i=0; i<commands.size()/2; i++){
            keyboardCommands.add(getCommandRow(commands.get(j), commands.get(j+1)));
            j+=2;
        }

        if(commands.size() % 2 == 1) keyboardCommands.add(getCommandRow(commands.get(j), null));

        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        replyKeyboard.setKeyboard(getKeyboardRows(keyboardCommands, contact));
        return replyKeyboard;
    }

    private List<KeyboardRow> getKeyboardRows(List<List<String>> commands, boolean contact) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for(List<String> commandsRow : commands) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (String command : commandsRow) {
                KeyboardButton keyboardButton = new KeyboardButton();
                keyboardButton.setText(command);
                keyboardButton.setRequestContact(contact);
                keyboardRow.add(keyboardButton);
            }
            keyboardRows.add(keyboardRow);
        }
        return keyboardRows;
    }

    private List<String> getCommandRow(String command1, String command2) {
        List<String> commandsRow = new ArrayList<>();
        commandsRow.add(command1);
        if(command2 != null) {
            commandsRow.add(command2);
        }
        return commandsRow;
    }

}
