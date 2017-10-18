package org.telegram.telegrambots.youtubeSearch;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.VideoAttributes;

import java.io.File;

public class AppManageMediaConverter {

	public Boolean convertToMp3(File source, File target) {

		EncodingAttributes attrs = convertVideoToMp3();

		Encoder encoder = new Encoder();
		try {
			System.out.println("Start conversion in mp3 format...");
			encoder.encode(source, target, attrs);
			System.out.println("File converted");
			return true;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error in file conversion", e);
		} catch (InputFormatException e) {
			throw new IllegalArgumentException("Error in file conversion, probably video is not in a correct format!", e);
		} catch (EncoderException e) {
			throw new IllegalArgumentException("Error in file conversion", e);
		}
	}

	public void convertToMp4(File source, File target) {

		EncodingAttributes attrs = convertVideoToMp4();

		Encoder encoder = new Encoder();
		try {
			System.out.println("Start conversion in mp4 format...");
			encoder.encode(source, target, attrs);
			System.out.println("File converted!");
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error in file conversion", e);
		} catch (InputFormatException e) {
			throw new IllegalArgumentException("Error in file conversion, probably video is not in a correct format!", e);
		} catch (EncoderException e) {
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
		attrs.setFormat(format);
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		return attrs;
	}

	private static VideoAttributes getVideoAttributes() {
		VideoAttributes video = new VideoAttributes();
		video.setBitRate(new Integer(160000));
		video.setFrameRate(new Integer(15));
		video.setCodec("mpeg4");
		video.setCodec(VideoAttributes.DIRECT_STREAM_COPY);
		return video;
	}

	private static AudioAttributes getAudioAttribute() {
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(128000));
		audio.setSamplingRate(new Integer(44100));
		audio.setChannels(new Integer(2));
		return audio;
	}
}
