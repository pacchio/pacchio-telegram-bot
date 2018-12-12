package com.mytelegrambot;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int SOCKET_TIMEOUT = 75 * 1000;

//    public static final String MONEYHONEY_PATH = "/home/pi/";
    public static final String MONEYHONEY_PATH = "D:\\Development\\workspace\\static-web-site\\moneyhoney\\automatic-runner\\";

//    public static String PHOTOS_FOLDER = "/home/pi/bot-resources/foto/";
//    public static String PHOTOS_FOLDER = "C:\\Development\\workspace\\java-project\\my-telegram-bot\\bot-resources\\foto";
    public static String PHOTOS_FOLDER = "D:\\Development\\workspace\\my-telegram-bot\\foto";

//    public static String AUDIO_FOLDER = "/home/pi/bot-resources/audio-video/";
//    public static String AUDIO_FOLDER = "C:\\Development\\workspace\\java-project\\my-telegram-bot\\bot-resources\\audio-video";
    public static String AUDIO_FOLDER = "D:\\Development\\workspace\\my-telegram-bot\\audio-video";

    public final static String SASSO = "Sasso\u270A";
    public final static String CARTA = "Carta\u270B";
    public final static String FORBICE = "Forbice\u270C";

    static List<String> RISPOSTE_TRASH = Arrays.asList("Tu sei ricco", "Che bella vita che fai", "Tu sei bellooo", "Vivi un sogno");
    public static List<String> RISPOSTE_VITTORIA = Arrays.asList("Madonna, che forte...", "Proprio bravo!", "Tanta roba...", "Bella giocata!");
    public static List<String> RISPOSTE_SCONFITTA = Arrays.asList("Dai magari la prossima volta" + Emoji.FACE_THROWING_A_KISS, "Ritenta, sarai più fortunato", "Ahahahahahaha, ciao dai", "Che scarso oh..." + Emoji.FACE_WITH_TEARS_OF_JOY);

    static List<String> CONTACT_COMMAND = Arrays.asList("Prosegui");
    static List<String> INIT_COMMANDS = Arrays.asList("Audio/Video", "Foto", "Gioca", "Messaggiamo", "Coin Market Cap", "Raspberry");
    static List<String> AUDIO_COMMANDS = Arrays.asList("Cerca su YouTube", "Indietro");
    static List<String> PHOTO_COMMANDS = Arrays.asList("Cerca su Google", "Indietro");
    static List<String> PLAY_COMMANDS = Arrays.asList(SASSO, CARTA, FORBICE, "Indietro");
    static List<String> TEXT_COMMANDS = Arrays.asList("Ciao", "Che ore sono?", "Che giorno è?", "Pajas", "Ti amo", "Indietro");
    static List<String> COINMARKETCAP_COMMANDS = Arrays.asList("Ciao", "Quote", "Disallineamenti", "Indietro");
    public static List<String> RASPBERRY_COMMANDS = Arrays.asList("MoneyHoney", "Toggle scheduling", "X", "Indietro");

    public static final String RASPBERRY_PWD = "pi";

    public static final String MESSAGE_COINMARKETCAP_FAILED = "Impossibile reperire le informazioni " + Emoji.CRYING_FACE;
    public static final String MESSAGE_AUDIO_FAILED = "Impossibile convertire questo video " + Emoji.CRYING_FACE;
    public static final String MESSAGE_VIDEO_FAILED = "Impossibile scaricare questo video " + Emoji.CRYING_FACE;

    static final String MESSAGE_INVIA_AUDIO = "Sto inviando l'audio... abbi pazienza! " + Emoji.KISSING_CAT_FACE_WITH_CLOSED_EYES;
    static final String MESSAGE_INVIA_AUDIO_IMPROVVISATO = "Scusa ma il video è troppo grande, quindi ti invio solamente l'audio " + Emoji.DISAPPOINTED_FACE;
    static final String MESSAGE_INVIA_VIDEO = "Sto inviando il video... abbi pazienza! " + Emoji.FACE_THROWING_A_KISS;
}
