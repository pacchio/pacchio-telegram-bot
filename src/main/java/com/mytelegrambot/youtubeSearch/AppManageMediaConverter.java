package com.mytelegrambot.youtubeSearch;

import lombok.extern.log4j.Log4j2;
import ws.schild.jave.*;

import java.io.File;

@Log4j2
public class AppManageMediaConverter {

	public Boolean convertToMp3(File source, File target) {
		EncodingAttributes attrs = convertVideoToMp3();
		Encoder encoder = new Encoder();
		try {
			log.info("Start conversion in mp3 format...");
			encoder.encode(new MultimediaObject(source), target, attrs);
			log.info("File converted");
			return true;
		} catch (InputFormatException e) {
			throw new IllegalArgumentException("Error in file conversion, probably video is not in a correct format!", e);
		} catch (IllegalArgumentException | EncoderException e) {
			throw new IllegalArgumentException("Error in file conversion", e);
		}
	}

	public void convertToMp4(File source, File target) {
		EncodingAttributes attrs = convertVideoToMp4();
		Encoder encoder = new Encoder();
		try {
			log.info("Start conversion in mp4 format...");
			encoder.encode(new MultimediaObject(source), target, attrs);
			log.info("File converted!");
		} catch (InputFormatException e) {
			throw new IllegalArgumentException("Error in file conversion, probably video is not in a correct format!", e);
		} catch (IllegalArgumentException | EncoderException e) {
			throw new IllegalArgumentException("Error in file conversion", e);
		}
	}

	private static EncodingAttributes convertVideoToMp4() {
		AudioAttributes audio = getAudioAttribute();
		VideoAttributes video = getVideoAttributes();
		return getEncodingAttributes(audio, video, "mp4");
	}

	private static EncodingAttributes convertVideoToMp3() {
		AudioAttributes audio = getAudioAttribute();
		return getEncodingAttributes(audio, null, "mp3");
	}

	private static EncodingAttributes getEncodingAttributes(AudioAttributes audio, VideoAttributes video, String format) {
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp4");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		return attrs;
	}

	private static VideoAttributes getVideoAttributes() {
		VideoAttributes video = new VideoAttributes();
		video.setCodec("h264");
		video.setX264Profile(VideoAttributes.X264_PROFILE.BASELINE);
		// Here 160 kbps video is 160000
		video.setBitRate(160000);
		// More the frames more quality and size, but keep it low based on devices like mobile
		video.setFrameRate(15);
		video.setSize(new VideoSize(400, 300));
		return video;
	}

	private static AudioAttributes getAudioAttribute() {
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("aac");
		// here 64kbit/s is 64000
		audio.setBitRate(64000);
		audio.setChannels(2);
		audio.setSamplingRate(44100);
		return audio;
	}
}
