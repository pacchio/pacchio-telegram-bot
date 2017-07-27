package org.telegram.telegrambots;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int SOCKET_TIMEOUT = 75 * 1000;

    static String RANDOM_PHOTOS_FOLDER = "D:\\Users\\arx50054\\Desktop\\Foto\\Random\\";
    static String CAT_PHOTOS_FOLDER = "D:\\Users\\arx50054\\Desktop\\Foto\\Gatti\\";
    static String MEME_PHOTOS_FOLDER = "D:\\Users\\arx50054\\Desktop\\Foto\\Meme\\";

    static List<String> RISPOSTE_TRASH = Arrays.asList("Tu sei ricco", "Che bella vita che fai", "Tu sei bellooo", "Vivi un sogno");
    static List<String> RISPOSTE_VITTORIA = Arrays.asList("Madonna, che forte...", "Proprio bravo!", "Tanta roba...", "Bella giocata!");
    static List<String> RISPOSTE_SCONFITTA = Arrays.asList("Dai magari la prossima volta", "Ritenta, sarai più fortunato", "Ahahahahahaha, ciao dai", "Che scarso oh...");

    static List<String> INIT_COMMANDS = Arrays.asList("Audio", "Foto", "Gioca", "Messaggiamo");
    static List<String> AUDIO_COMMANDS = Arrays.asList("xxx", "yyy", "zzz", "Indietro");
    static List<String> PHOTO_COMMANDS = Arrays.asList("Meme", "Gatti", "Random", "Indietro");
    static List<String> PLAY_COMMANDS = Arrays.asList(MorraCinese.SASSO.getDescrizione(), MorraCinese.CARTA.getDescrizione(), MorraCinese.FORBICE.getDescrizione(), "Indietro");
    static List<String> TEXT_COMMANDS = Arrays.asList("Ciao", "Che ore sono?", "Che giorno è?", "Pajas", "Ti amo", "Indietro");
}
