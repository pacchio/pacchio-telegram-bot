package com.mytelegrambot.simpleMessages;

import com.mytelegrambot.KeyboardManager;
import com.mytelegrambot.MessageManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.mytelegrambot.ExceptionHelper.getExceptionStacktrace;

@Component
@Log4j2
public class SimpleMessagesManager {

    private KeyboardManager keyboardManager;
    private MessageManager messageManager;

    @Autowired
    public SimpleMessagesManager(KeyboardManager keyboardManager, MessageManager messageManager) {
        this.keyboardManager=keyboardManager;
        this.messageManager=messageManager;
    }

    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar now = Calendar.getInstance();
        return sdf.format(now.getTime());
    }

    public String getDate(){
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
            log.error(getExceptionStacktrace(e));
        }

        // Then get the day of week from the Date based on specific locale.
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ITALY).format(date);

        return dayOfWeek + ", " + day + "/" + month + "/" + year;
    }
}
