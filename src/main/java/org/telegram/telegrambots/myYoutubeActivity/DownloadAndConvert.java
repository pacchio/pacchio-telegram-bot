package org.telegram.telegrambots.myYoutubeActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DownloadAndConvert {

	private static String path = "D:\\Users\\arx50054\\Desktop\\prove\\";

	public Object startAudio(String url){
		try {
			AppManagedDownload appManagedDownload = new AppManagedDownload();
			List<String> videoTitles = appManagedDownload.download(url, path);
			AppManageMediaConverter appManageMediaConverter = new AppManageMediaConverter();
			Boolean conversionResult = false;
			File target = null;
			for (String videoTitle : videoTitles) {
				System.out.println("Trying with " + videoTitle);
				File source = new File(path + videoTitle);
				target = builtTargetForMp3(path, videoTitle);
				if (target == null) {
					return "Il formato del video non è in uno dei formati accettati, quindi non potrà essere convertito!";
				} else {
					try {
						conversionResult = appManageMediaConverter.convertToMp3(source, target);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				eliminaFile(videoTitle, path);
			}
			if(conversionResult){
				return target;
			}
			else{
				return "Download o conversione falliti";
			}
		}
		catch(Exception e){
			return e.getMessage();
		}
	}

	public Object startVideo(String url){
		try {
			AppManagedDownload appManagedDownload = new AppManagedDownload();
			List<String> videoTitles = appManagedDownload.download(url, path);
			for (String videoTitle : videoTitles) {
				System.out.println("Trying with " + videoTitle);
				if (videoTitle.contains(".mp4")) {
					return new File(path + videoTitle);
				}
			}
			return "Download non riuscito correttamente";
		}
		catch(Exception e){
			return e.getMessage();
		}
	}

	private File builtTargetForMp3(String path, String videoTitle){
		if(videoTitle.contains(".mp4")) {
			return new File(path + videoTitle.replace(".mp4", ".mp3"));
		}
		else if(videoTitle.contains(".avi")) {
			return new File(path + videoTitle.replace(".avi", ".mp3"));
		}
		return null;
	}

	private void eliminaFile(String videoTitle, String path) throws IOException {
		File file = new File(path + videoTitle);
		try {
			Boolean deleted = file.delete();
			if(deleted){
				System.out.println("Video deleted.");
			}
			else{
				System.out.println("Video not deleted.");
			}
		}
		catch (Exception e){
			System.out.println("Error deleting video");
		}
	}
}
