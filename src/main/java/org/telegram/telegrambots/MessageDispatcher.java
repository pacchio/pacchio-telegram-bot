package org.telegram.telegrambots;

import static org.telegram.telegrambots.Constants.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.api.services.youtube.model.SearchListResponse;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.googleSearch.DowloadImageManager;
import org.telegram.telegrambots.googleSearch.GResult;
import org.telegram.telegrambots.googleSearch.GoogleSearchService;
import org.telegram.telegrambots.googleSearch.ImageInfo;
import org.telegram.telegrambots.youtubeSearch.DownloadAndConvert;
import org.telegram.telegrambots.youtubeSearch.MySingletonMap;
import org.telegram.telegrambots.youtubeSearch.YoutubeService;

class MessageDispatcher {

    private KeyboardManager keyboardManager = new KeyboardManager();
     private MessageManager messageManager = new MessageManager();

     Object createResponse(Update update) {

        switch (update.getMessage().getText()){
            case "/start": return keyboardManager.createKeyboardMessage(Constants.INIT_COMMANDS);

            case "Audio/Video":   return keyboardManager.createKeyboardMessage(Constants.AUDIO_COMMANDS);
            case "Foto":    return keyboardManager.createKeyboardMessage(Constants.PHOTO_COMMANDS);
            case "Gioca":   return keyboardManager.createKeyboardMessage(Constants.PLAY_COMMANDS);
            case "Messaggiamo":   return keyboardManager.createKeyboardMessage(Constants.TEXT_COMMANDS);

            case "Ciao":            return messageManager.getSendMessage("Ciao Bambolina" + Emoji.FACE_THROWING_A_KISS);
            case "Che ore sono?":   return messageManager.getSendMessage(getTime());
            case "Che giorno è?":   return messageManager.getSendMessage(getDate());
            case "Pajas":           return messageManager.getSendMessage(getRandomSentence(Constants.RISPOSTE_TRASH));
            case "Ti amo":          return messageManager.getSendMessage("Anche io" + Emoji.SMILING_FACE_WITH_HEART_SHAPED_EYES + Emoji.RED_HEARTH);

            case "Cerca su Google": return messageManager.getSendMessage("Scrivi l'immagine da cercare preceduta da '?'\nL'immagine inviata è casuale quindi se non sei soddisfatto ritenta la ricerca! " + Emoji.DIZZY_FACE);

            case SASSO:   return messageManager.getSendMessage(getGame(update.getMessage().getChatId(), SASSO));
            case CARTA:   return messageManager.getSendMessage(getGame(update.getMessage().getChatId(), CARTA));
            case FORBICE: return messageManager.getSendMessage(getGame(update.getMessage().getChatId(), FORBICE));

            case "Cerca su YouTube": return messageManager.getSendMessage("Scrivi il video da cercare preceduto da '#'");

            case "Indietro": return keyboardManager.createKeyboardMessage(Constants.INIT_COMMANDS);

            default: return controllaDefault(update.getMessage());
        }
    }

    private Object controllaDefault(Message message) {
        MyBot myBot = new MyBot();
        if(message.getText().startsWith("#")){
            return messageManager.getSendMessage(creaResponseRicercaDaYoutube(message.getText().substring(1)));
        }
        else if(message.getText().startsWith("-")){
            myBot.inviaMessaggio(message.getChatId(), messageManager.getSendMessage("Sto scaricando..."));
            Object responseDownload = creaResponseDownloadAudio(message.getText().substring(1));
            if(responseDownload instanceof File){
                myBot.inviaMessaggio(message.getChatId(), messageManager.getSendMessage(MESSAGE_INVIA_AUDIO));
                return messageManager.getSendAudio((File) responseDownload);
            }
            else if(responseDownload instanceof String) {
                return messageManager.getSendMessage((String) responseDownload);
            }
        }
        else if(message.getText().startsWith("!")){
            myBot.inviaMessaggio(message.getChatId(), messageManager.getSendMessage("Sto scaricando..."));
            Object responseDownload = creaResponseDownloadVideo(message.getText().substring(1));
            if(responseDownload instanceof File){
                if(((File) responseDownload).getName().contains(".webm")){
                    myBot.inviaMessaggio(message.getChatId(), messageManager.getSendMessage(MESSAGE_INVIA_AUDIO_IMPROVVISATO));
                    return messageManager.getSendAudio((File) responseDownload);
                }
                else{
                    myBot.inviaMessaggio(message.getChatId(), messageManager.getSendMessage(MESSAGE_INVIA_VIDEO));
                    return messageManager.getSendVideo((File) responseDownload);
                }
            }
            else if(responseDownload instanceof String) {
                return messageManager.getSendMessage((String) responseDownload);
            }
        }
        else if(message.getText().startsWith("?")){
            Object responseDownload = creaResponseRicercaDaGoogle(sistemaStrinaRicercaGoogle(message));
            if(responseDownload instanceof File) {
                return messageManager.getSendPhoto((File) responseDownload);
            }
            else{
                return messageManager.getSendMessage((String) responseDownload);
            }
        }
        return messageManager.getSendMessage("Il messaggio '" + message.getText() + "' non corrisponde ad alcuna funzione");
    }

    private String sistemaStrinaRicercaGoogle(Message message) {
        return message.getText().substring(1).replaceAll(" ", "%20");
    }

    private Object creaResponseDownloadAudio(String index) {
        DownloadAndConvert downloadAndConvert = new DownloadAndConvert();
		return downloadAndConvert.startAudio(MySingletonMap.getInstance().getMap().get(Integer.parseInt(index)));
    }

    private Object creaResponseDownloadVideo(String index) {
        DownloadAndConvert downloadAndConvert = new DownloadAndConvert();
        return downloadAndConvert.startVideo(MySingletonMap.getInstance().getMap().get(Integer.parseInt(index)));
    }

    private String creaResponseRicercaDaYoutube(String chiaveDiRicerca) {
        YoutubeService youtubeService = new YoutubeService();
        try {
            SearchListResponse response = youtubeService.ricercaSuYoutube(chiaveDiRicerca);
            return youtubeService.visualizzaListaRisultati(response);
        } catch (IOException e) {
            return "Errore durante la ricerca " + Emoji.CRYING_CAT_FACE;
        }
    }

    private Object creaResponseRicercaDaGoogle(String chiaveDiRicerca) {
        GoogleSearchService googleSearchService = new GoogleSearchService();
        DowloadImageManager dowloadImageManager = new DowloadImageManager();
        try {
            List<GResult> results = googleSearchService.search(chiaveDiRicerca);
            Map<Integer, ImageInfo> resultsUrl = googleSearchService.creaListaRisultati(results);
            ImageInfo imageInfo = resultsUrl.get(new Random().nextInt(10));
            return dowloadImageManager.download(imageInfo.getUrl(), imageInfo.getFilename());
        } catch (Exception e) {
            return e.getMessage() + Emoji.CRYING_CAT_FACE;
        }
    }

    private String getGame(Long chatId, String sceltaGiocatore) {
        List<String> sceltePossibili = Arrays.asList(SASSO, CARTA, FORBICE);
        String sceltaPc = sceltePossibili.get(new Random().nextInt(sceltePossibili.size()));
        new MyBot().inviaMessaggio(chatId, messageManager.getSendMessage(sceltaPc));

        if(sceltaGiocatore.equals(sceltaPc)){
            return "PAREGGIO" + Emoji.SMILING_FACE_WITH_OPEN_MOUTH_AND_SMILING_EYES;
        }
        else{
            switch (sceltaGiocatore){
                case SASSO: return sceltaPc.equals(FORBICE) ? "HAI VINTO\n"
                        + getRandomSentence(Constants.RISPOSTE_VITTORIA) : "HAI PERSO\n"
                        + getRandomSentence(Constants.RISPOSTE_SCONFITTA);
                case CARTA: return sceltaPc.equals(SASSO) ? "HAI VINTO\n"
                        + getRandomSentence(Constants.RISPOSTE_VITTORIA) : "HAI PERSO\n"
                        + getRandomSentence(Constants.RISPOSTE_SCONFITTA);
                case FORBICE: return sceltaPc.equals(CARTA) ? "HAI VINTO\n"
                        + getRandomSentence(Constants.RISPOSTE_VITTORIA) : "HAI PERSO\n"
                        + getRandomSentence(Constants.RISPOSTE_SCONFITTA);
            }
        }
        return null;
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
