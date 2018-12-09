package com.mytelegrambot;

import java.util.List;
import java.util.Random;

public class Helper {

    public static String getRandomSentence(List<String> listaRisposte) {
        return listaRisposte.get(new Random().nextInt(listaRisposte.size()));
    }

}
