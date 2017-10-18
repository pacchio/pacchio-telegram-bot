package org.telegram.telegrambots.youtubeSearch;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.telegram.telegrambots.Constants.AUDIO_FOLDER;
import static org.telegram.telegrambots.Constants.MESSAGE_AUDIO_FAILED;
import static org.telegram.telegrambots.Constants.MESSAGE_VIDEO_FAILED;

public class DownloadAndConvert {

	public Object startAudio(String url){
		try {
			AppManagedDownload appManagedDownload = new AppManagedDownload();
			List<File> videoTitles = appManagedDownload.download(url, AUDIO_FOLDER);
			AppManageMediaConverter appManageMediaConverter = new AppManageMediaConverter();
			for (File file : videoTitles) {
				System.out.println("Trying with " + file);
				File source = new File(file.getAbsolutePath());
				File target = builtTargetForMp3(file.getAbsolutePath());
				if(target != null) {
					try {
						Boolean conversionResult = appManageMediaConverter.convertToMp3(source, target);
						if(conversionResult){
							eliminaFile(file.getAbsolutePath());
							return target;
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				else {
					return new File(file.getAbsolutePath());
				}
			}
			return MESSAGE_AUDIO_FAILED;
		}
		catch(Exception e){
			return MESSAGE_AUDIO_FAILED;
		}
	}

	public Object startVideo(String url){
		try {
			AppManagedDownload appManagedDownload = new AppManagedDownload();
			List<File> videoTitles = appManagedDownload.download(url, AUDIO_FOLDER);
			for (File file : videoTitles) {
				System.out.println("Trying with " + file.getName());
				if (new File(file.getAbsolutePath()).canExecute() && file.length() < 40000000L) {
					return new File(file.getAbsolutePath());
				}
			}
			return MESSAGE_VIDEO_FAILED;
		}
		catch(Exception e){
			return MESSAGE_VIDEO_FAILED;
		}
	}

	private File builtTargetForMp3(String fullPath){
		if(fullPath.contains(".mp4")) {
			return new File(fullPath.replace(".mp4", ".mp3"));
		}
		else if(fullPath.contains(".avi")) {
			return new File(fullPath.replace(".avi", ".mp3"));
		}
		return null;
	}

	private void eliminaFile(String fullPath) throws IOException {
		File file = new File(fullPath);
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
