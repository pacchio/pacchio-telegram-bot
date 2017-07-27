package org.telegram.telegrambots;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.telegram.telegrambots.api.objects.Update;

class MessageDispatcher {
    
     private KeyboardManager keyboardManager = new KeyboardManager();
     private MessageManager messageManager = new MessageManager();

     Object createResponse(Update update) {

        switch (update.getMessage().getText()){
            case "/start": return keyboardManager.createKeyboardMessage(Constants.INIT_COMMANDS);

            case "Audio":   return keyboardManager.createKeyboardMessage(Constants.AUDIO_COMMANDS);
            case "Foto":    return keyboardManager.createKeyboardMessage(Constants.PHOTO_COMMANDS);
            case "Gioca":   return keyboardManager.createKeyboardMessage(Constants.PLAY_COMMANDS);
            case "Messaggiamo":   return keyboardManager.createKeyboardMessage(Constants.TEXT_COMMANDS);

            case "Ciao":            return messageManager.createSendMessage("Ciao Bambolina" + Emoji.FACE_THROWING_A_KISS);
            case "Che ore sono?":   return messageManager.createSendMessage(getTime());
            case "Che giorno è?":   return messageManager.createSendMessage(getDate());
            case "Pajas":           return messageManager.createSendMessage(getRandomSentence(Constants.RISPOSTE_TRASH));
            case "Ti amo":          return messageManager.createSendMessage("Anche io" + Emoji.SMILING_FACE_WITH_HEART_SHAPED_EYES + Emoji.RED_HEARTH);

            case "Random":  return messageManager.getSendPhoto(Constants.RANDOM_PHOTOS_FOLDER);
            case "Gatti":   return messageManager.getSendPhoto(Constants.CAT_PHOTOS_FOLDER);
            case "Meme":    return messageManager.getSendPhoto(Constants.MEME_PHOTOS_FOLDER);

            case "Sasso":   return messageManager.createSendMessage(getGame(update.getMessage().getChatId(), "Sasso"));
            case "Carta":   return messageManager.createSendMessage(getGame(update.getMessage().getChatId(), "Carta"));
            case "Forbice": return messageManager.createSendMessage(getGame(update.getMessage().getChatId(), "Forbice"));

            case "Indietro": return keyboardManager.createKeyboardMessage(Constants.INIT_COMMANDS);

            default: return messageManager.createSendMessage("Il messaggio '" + update.getMessage().getText() + "' non corrisponde ad alcuna funzione");
        }
    }

    private String getGame(Long chatId, String sceltaGiocatore) {
        List<MorraCinese> sceltePossibili = Arrays.asList(MorraCinese.SASSO, MorraCinese.CARTA, MorraCinese.FORBICE);
        MorraCinese sceltaPc = sceltePossibili.get(new Random().nextInt(sceltePossibili.size()));
        new MyBot().inviaMessaggio(chatId, messageManager.createSendMessage(creaRispostaConEmoji(sceltaPc)));

        if(MorraCinese.fromString(sceltaGiocatore).equals(sceltaPc)){
            return "PAREGGIO" + Emoji.SMILING_FACE_WITH_OPEN_MOUTH_AND_SMILING_EYES;
        }
        else{
            switch (sceltaGiocatore){
                case "Sasso": return sceltaPc.equals(MorraCinese.FORBICE) ? "HAI VINTO\n"
                        + getRandomSentence(Constants.RISPOSTE_VITTORIA) : "HAI PERSO\n"
                        + getRandomSentence(Constants.RISPOSTE_SCONFITTA);
                case "Carta": return sceltaPc.equals(MorraCinese.SASSO) ? "HAI VINTO\n"
                        + getRandomSentence(Constants.RISPOSTE_VITTORIA) : "HAI PERSO\n"
                        + getRandomSentence(Constants.RISPOSTE_SCONFITTA);
                case "Forbice": return sceltaPc.equals(MorraCinese.CARTA) ? "HAI VINTO\n"
                        + getRandomSentence(Constants.RISPOSTE_VITTORIA) : "HAI PERSO\n"
                        + getRandomSentence(Constants.RISPOSTE_SCONFITTA);
            }
        }
        return null;
    }

    private String creaRispostaConEmoji(MorraCinese sceltaPc) {
        String risposta = "";
        if(sceltaPc == MorraCinese.SASSO){
            risposta = MorraCinese.SASSO.getDescrizione() + Emoji.RAISED_FIST;
        }
        else if(sceltaPc == MorraCinese.CARTA){
            risposta = MorraCinese.CARTA.getDescrizione() + Emoji.RAISED_HAND;
        }
        else if(sceltaPc == MorraCinese.FORBICE){
            risposta = MorraCinese.FORBICE.getDescrizione() + Emoji.VICTORY_HAND;
        }
        return risposta;
    }

    private String getRandomSentence(List<String> listaRisposte) {
        return listaRisposte.get(new Random().nextInt(listaRisposte.size()));
    }

    private String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar now = Calendar.getInstance();
        return sdf.format(now.getTime());
    }

    private String getDate(){
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
            e.printStackTrace();
        }

        // Then get the day of week from the Date based on specific locale.
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ITALY).format(date);

        return dayOfWeek + ", " + day + "/" + month + "/" + year;
    }
}
