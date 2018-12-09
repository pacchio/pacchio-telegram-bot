package com.mytelegrambot;

import com.mytelegrambot.coinmarketcap.CoinMarketCap;
import com.mytelegrambot.googleSearch.GoogleSearchManager;
import com.mytelegrambot.play.PlayManager;
import com.mytelegrambot.raspberry.RaspberryManager;
import com.mytelegrambot.simpleMessages.SimpleMessagesManager;
import com.mytelegrambot.youtubeSearch.DownloadAndConvert;
import com.mytelegrambot.youtubeSearch.MySingletonMap;
import com.mytelegrambot.youtubeSearch.YoutubeSearchManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.io.File;

import static com.mytelegrambot.Constants.*;
import static com.mytelegrambot.Helper.getRandomSentence;

@Log4j2
@Component
class MessageDispatcher {

    @Value("${url}")
    String apiUrl;

    @Autowired
    private KeyboardManager keyboardManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private MyBot myBot;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SimpleMessagesManager simpleMessagesManager;
    @Autowired
    private PlayManager playManager;
    @Autowired
    private GoogleSearchManager googleSearchManager;
    @Autowired
    private YoutubeSearchManager youtubeSearchManager;
    @Autowired
    private RaspberryManager raspberryManager;

     Object createResponse(Update update) {

        if(update.getMessage().getContact() != null){
            TelegramUserData request = createRequest(update.getMessage());
            TelegramUserData telegramUserData = restTemplate.postForObject(apiUrl, request, TelegramUserData.class);
            if(telegramUserData != null) {
                log.info("Insert new user: " + telegramUserData.chatID);
            } else {
                log.info("User already exist");
            }
            return keyboardManager.createKeyboardMessage(Constants.INIT_COMMANDS);
        }

        switch (update.getMessage().getText()){
            case "/start": return keyboardManager.createKeyboardContact(Constants.CONTACT_COMMAND);

            case "Audio/Video":   return keyboardManager.createKeyboardMessage(Constants.AUDIO_COMMANDS);
            case "Foto":    return keyboardManager.createKeyboardMessage(Constants.PHOTO_COMMANDS);
            case "Gioca":   return keyboardManager.createKeyboardMessage(Constants.PLAY_COMMANDS);
            case "Messaggiamo":   return keyboardManager.createKeyboardMessage(Constants.TEXT_COMMANDS);
            case "Coin Market Cap":   return keyboardManager.createKeyboardMessage(Constants.COINMARKETCAP_COMMANDS);
            case "Raspberry":   return messageManager.getSendMessage("Inserisci la password preceduta da '_'");

            case "Ciao":            return messageManager.getSendMessage("Ciao Bambolina" + Emoji.FACE_THROWING_A_KISS);
            case "Che ore sono?":   return messageManager.getSendMessage(simpleMessagesManager.getTime());
            case "Che giorno è?":   return messageManager.getSendMessage(simpleMessagesManager.getDate());
            case "Pajas":           return messageManager.getSendMessage(getRandomSentence(Constants.RISPOSTE_TRASH));
            case "Ti amo":          return messageManager.getSendMessage("Anche io" + Emoji.SMILING_FACE_WITH_HEART_SHAPED_EYES + Emoji.RED_HEARTH);

            case "Cerca su Google": return messageManager.getSendMessage("Scrivi l'immagine da cercare preceduta da '?'\nL'immagine inviata è casuale quindi se non sei soddisfatto ritenta la ricerca! " + Emoji.DIZZY_FACE);

            case SASSO:   return messageManager.getSendMessage(playManager.getGame(update.getMessage().getChatId(), SASSO));
            case CARTA:   return messageManager.getSendMessage(playManager.getGame(update.getMessage().getChatId(), CARTA));
            case FORBICE: return messageManager.getSendMessage(playManager.getGame(update.getMessage().getChatId(), FORBICE));

            case "Quote": return messageManager.getSendMessage(new CoinMarketCap().info());
            case "Disallineamenti": return messageManager.getSendMessage(new CoinMarketCap().disallinamenti());

            case "Cerca su YouTube": return messageManager.getSendMessage("Scrivi il video da cercare preceduto da '#'");

            case "Indietro": return keyboardManager.createKeyboardMessage(Constants.INIT_COMMANDS);

            default: return controllaDefault(update.getMessage());
        }
    }

    private TelegramUserData createRequest(Message message) {
        TelegramUserData telegramUserData = new TelegramUserData();
        telegramUserData.setChatID(message.getChatId());
        telegramUserData.setFirstname(message.getChat().getFirstName());
        telegramUserData.setLastname(message.getChat().getLastName());
        telegramUserData.setUsername(message.getChat().getUserName());
        telegramUserData.setTelephoneNumber(message.getContact().getPhoneNumber());
        return telegramUserData;
    }

    private Object controllaDefault(Message message) {
        if(message.getText().startsWith("#")){
            return youtubeSearchManager.youtubeManager(message);
        } else if(message.getText().startsWith("_")) {
            return raspberryManager.manageRaspberryAuthentication(message);
        } else if(!StringUtils.isEmpty(raspberryManager.getRasperryToken()) && message.getText().contains(raspberryManager.getRasperryToken())) {
            return raspberryManager.manageRaspberry(message);
        } else if(message.getText().startsWith("-")){
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
            return googleSearchManager.manageResearchOnGoogle(message);
        }
        return getDefaultMessage(message);
    }

    private SendMessage getDefaultMessage(Message message) {
        return messageManager.getSendMessage("Il messaggio '" + message.getText() + "' non corrisponde ad alcuna funzione");
    }

    private Object creaResponseDownloadAudio(String index) {
        DownloadAndConvert downloadAndConvert = new DownloadAndConvert();
		return downloadAndConvert.startAudio(MySingletonMap.getInstance().getMap().get(Integer.parseInt(index)));
    }

    private Object creaResponseDownloadVideo(String index) {
        DownloadAndConvert downloadAndConvert = new DownloadAndConvert();
        return downloadAndConvert.startVideo(MySingletonMap.getInstance().getMap().get(Integer.parseInt(index)));
    }
}
