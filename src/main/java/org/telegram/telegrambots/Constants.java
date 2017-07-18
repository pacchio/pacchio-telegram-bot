package org.telegram.telegrambots;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int SOCKET_TIMEOUT = 75 * 1000;

    public static List<String> INIT_COMMANDS = Arrays.asList("Audio", "Foto", "Video", "Testo");
    public static List<String> AUDIO_COMMANDS = Arrays.asList("xxx", "yyy", "zzz", "Indietro", "dd", "ff");
    public static List<String> PHOTO_COMMANDS = Arrays.asList("xxx", "yyy", "zzz", "Indietro");
    public static List<String> VIDEO_COMMANDS = Arrays.asList("xxx", "yyy", "zzz", "Indietro");
    public static List<String> TEXT_COMMANDS = Arrays.asList("Ciao", "Ora", "Data", "Indietro");
}
