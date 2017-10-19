package org.telegram.telegrambots.googleSearch;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.telegram.telegrambots.Constants.PHOTOS_FOLDER;

public class DowloadImageManager {

	final Logger logger = Logger.getLogger(DowloadImageManager.class);

	public File download(String link, String filename) throws MalformedURLException {
		URL url = new URL(link);
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			FileOutputStream outputStream = new FileOutputStream(PHOTOS_FOLDER + File.separator + filename);
			InputStream inputStream = connection.getInputStream();

			int read = -1;
			byte[] buffer = new byte[4096];

			while ((read = inputStream.read(buffer)) != -1){
				outputStream.write(buffer, 0, read);
				logger.info("File is downloading...");
			}

			inputStream.close();
			outputStream.close();

			logger.info("Download completed !");

			return new File(PHOTOS_FOLDER + File.separator + filename);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore durante il download!");
		}
	}

}
