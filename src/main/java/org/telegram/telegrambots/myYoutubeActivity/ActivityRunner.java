package org.telegram.telegrambots.myYoutubeActivity;

import com.google.api.services.youtube.model.SearchListResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class ActivityRunner {



	public static void main(String[] args) throws IOException {

		String path = "D:\\Users\\arx50054\\Desktop\\prove\\";

		Scanner in = new Scanner(System.in);

		System.out.print("Inserisci il video da cercare su youtube: ");
		String chiaveDiRicerca = in.nextLine();
		YoutubeService youtubeService = new YoutubeService();
		SearchListResponse response = youtubeService.ricercaSuYoutube(chiaveDiRicerca);
		String videoUrl = youtubeService.visualizzaListaRisultati(response);

		System.out.print("Seleziona un url: ");

//
//		DownloadAndConvert downloadAndConvert = new DownloadAndConvert();
//		downloadAndConvert.startAudio(url, path);
	}

}
