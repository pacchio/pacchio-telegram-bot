package org.telegram.telegrambots.googleSearch;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.telegram.telegrambots.Constants.PHOTOS_FOLDER;

public class DowloadImageManager {

	final Logger logger = Logger.getLogger(DowloadImageManager.class);

	public File download(String link, String filename) throws MalformedURLException {
		if(getPhotoWithSameTitle(PHOTOS_FOLDER, filename).size() == 0) {
			URL url = new URL(link);
			try {
				logger.info("Download started !");
				BufferedImage img = ImageIO.read(url);
				File file = new File(PHOTOS_FOLDER + File.separator + filename);
				ImageIO.write(img, "jpg", file);
				logger.info("Download completed !");
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Errore durante il download!");
			}
		}
		else{
			logger.info("Foto già esistente in archivio");
		}
		return new File(PHOTOS_FOLDER + File.separator + filename);
	}

	private List<File> getPhotoWithSameTitle(String path, String title) {
		File folder = new File(path);
		List<File> files = new ArrayList<>();
		if (folder.isDirectory()) {
			File[] listOfFiles = folder.listFiles();
			if(listOfFiles == null){
				return files;
			}
			for(File file : listOfFiles){
				if(file.getName().contains(title)){
					files.add(file);
				}
			}
		}
		return files;
	}

}
