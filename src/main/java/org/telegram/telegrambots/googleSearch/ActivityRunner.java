package org.telegram.telegrambots.googleSearch;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ActivityRunner {
	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(System.in);

		System.out.print("Inserisci l'immagine da cercare su Google: ");
		String chiaveDiRicerca = in.nextLine();

		GoogleSearchService googleSearchService = new GoogleSearchService();
		DowloadImageManager dowloadImageManager = new DowloadImageManager();

		try {
			List<GResult> results = googleSearchService.search(chiaveDiRicerca);
			Map<Integer, ImageInfo> resultsUrl = googleSearchService.creaListaRisultati(results);
			System.out.print("Inserisci quale immagine scaricare: ");
			ImageInfo imageInfo = resultsUrl.get(in.nextInt());
			dowloadImageManager.download(imageInfo.getUrl(), imageInfo.getFilename());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
