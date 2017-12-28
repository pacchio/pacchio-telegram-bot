package com.mytelegrambot;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHelper {

    public static String getExceptionStacktrace(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

}
