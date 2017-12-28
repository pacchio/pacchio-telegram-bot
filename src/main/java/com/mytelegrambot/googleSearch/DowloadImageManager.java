package com.mytelegrambot.googleSearch;

import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.mytelegrambot.Constants.PHOTOS_FOLDER;
import static com.mytelegrambot.ExceptionHelper.getExceptionStacktrace;

@Log4j2
public class DowloadImageManager {

	public File download(String link, String filename) throws MalformedURLException {
		if(getPhotoWithSameTitle(PHOTOS_FOLDER, filename).size() == 0) {
			URL url = new URL(link);
			try {
				log.info("Download started !");
				BufferedImage img = ImageIO.read(url);
				File file = new File(PHOTOS_FOLDER + File.separator + filename);
				ImageIO.write(img, "jpg", file);
				log.info("Download completed !");
			} catch (IOException e) {
				log.error(getExceptionStacktrace(e));
				throw new RuntimeException("Errore durante il download!");
			}
		}
		else{
			log.info("Foto già esistente in archivio");
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
