package org.telegram.telegrambots.youtubeSearch;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoFileInfo;
import com.github.axet.vget.info.VideoInfo;
import com.github.axet.vget.vhs.VimeoInfo;
import com.github.axet.vget.vhs.YouTubeInfo;
import com.github.axet.wget.SpeedInfo;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.ex.DownloadInterruptedError;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppManagedDownload {

	static class VGetStatus implements Runnable {
		VideoInfo videoinfo;
		long last;

		Map<VideoFileInfo, SpeedInfo> map = new HashMap<VideoFileInfo, SpeedInfo>();

		public VGetStatus(VideoInfo i) {
			this.videoinfo = i;
		}

		public SpeedInfo getSpeedInfo(VideoFileInfo dinfo) {
			SpeedInfo speedInfo = map.get(dinfo);
			if (speedInfo == null) {
				speedInfo = new SpeedInfo();
				speedInfo.start(dinfo.getCount());
				map.put(dinfo, speedInfo);
			}
			return speedInfo;
		}

		@Override
		public void run() {
			List<VideoFileInfo> dinfoList = videoinfo.getInfo();

			// notify app or save download state
			// you can extract information from DownloadInfo info;
			switch (videoinfo.getState()) {
				case EXTRACTING:
				case EXTRACTING_DONE:
				case DONE:
					if (videoinfo instanceof YouTubeInfo) {
						YouTubeInfo i = (YouTubeInfo) videoinfo;
						System.out.println(videoinfo.getState() + " " + i.getVideoQuality());
					} else if (videoinfo instanceof VimeoInfo) {
						VimeoInfo i = (VimeoInfo) videoinfo;
						System.out.println(videoinfo.getState() + " " + i.getVideoQuality());
					} else {
						System.out.println("downloading unknown quality");
					}
					for (VideoFileInfo d : videoinfo.getInfo()) {
						SpeedInfo speedInfo = getSpeedInfo(d);
						speedInfo.end(d.getCount());
						if(d.targetFile != null) {
							System.out.println(String.format("file:%d - %s (%s)", dinfoList.indexOf(d), d.targetFile.getName(),
									formatSpeed(speedInfo.getAverageSpeed())));
						}
						else{
							System.out.println(String.format("file:%d - %s (%s)", dinfoList.indexOf(d), d.targetFile,
									formatSpeed(speedInfo.getAverageSpeed())));
						}
					}
					break;
				case ERROR:
						System.out.println(videoinfo.getState() + " " + videoinfo.getDelay());

						if (dinfoList != null) {
							for (DownloadInfo dinfo : dinfoList) {
								System.out.println("file:" + dinfoList.indexOf(dinfo) + " - " + dinfo.getException() + " delay:"
										+ dinfo.getDelay());
							}
						}
						break;
				case RETRYING:
					System.out.println(videoinfo.getState() + " " + videoinfo.getDelay());

					if (dinfoList != null) {
						for (DownloadInfo dinfo : dinfoList) {
							System.out.println("file:" + dinfoList.indexOf(dinfo) + " - " + dinfo.getState() + " "
									+ dinfo.getException() + " delay:" + dinfo.getDelay());
						}
					}
					if(videoinfo.getDelay() > 3) {
						throw new RuntimeException("Download non riuscito");
					}
					break;
				case DOWNLOADING:
					long now = System.currentTimeMillis();
					if (now - 1000 > last) {
						last = now;

						for (VideoFileInfo dinfo : dinfoList) {
							SpeedInfo speedInfo = getSpeedInfo(dinfo);
							speedInfo.step(dinfo.getCount());

							System.out.println(String.format("file:%d - %s %.0f%% (%s)", dinfoList.indexOf(dinfo),
									videoinfo.getState(), (dinfo.getCount() / (float) dinfo.getLength())*100,
									formatSpeed(speedInfo.getCurrentSpeed())));
						}
					}
					break;
				default:
					break;
			}
		}
	}

	public static String formatSpeed(long s) {
		if (s > 0.1 * 1024 * 1024 * 1024) {
			float f = s / 1024f / 1024f / 1024f;
			return String.format("%.1f GB/s", f);
		} else if (s > 0.1 * 1024 * 1024) {
			float f = s / 1024f / 1024f;
			return String.format("%.1f MB/s", f);
		} else {
			float f = s / 1024f;
			return String.format("%.1f kb/s", f);
		}
	}

	public List<File> download(String url, String path) {
		File destinationTarget = new File(path);

		try {
			final AtomicBoolean stop = new AtomicBoolean(false);

			URL web = new URL(url);

			// [OPTIONAL] limit maximum quality, or do not call this function if
			// you wish maximum quality available.
			//
			// if youtube does not have video with requested quality, program
			// will raise en exception.
			VGetParser user = null;

			// create proper html parser depends on url
			user = VGet.parser(web);

			// download limited video quality from youtube
			// user = new YouTubeQParser(YoutubeQuality.p480);

			// download mp4 format only, fail if non exist
//			user = new YouTubeMPGParser();

			// create proper videoinfo to keep specific video information
			VideoInfo videoinfo = user.info(web);

			VGet v = new VGet(videoinfo, destinationTarget);

			VGetStatus notify = new VGetStatus(videoinfo);

			// [OPTIONAL] call v.extract() only if you d like to get video title
			// or download url link before startAudio download. or just skip it.
			v.extract(user, stop, notify);

			System.out.println("Title: " + videoinfo.getTitle());

			List<File> videos = getVideosWithSameTitle(path, videoinfo.getTitle());
			if(videos.size() > 0){
				return videos;
			}

			List<VideoFileInfo> list = videoinfo.getInfo();
			if (list != null) {
				for (VideoFileInfo d : list) {
					// [OPTIONAL] setTarget destinationTarget for each download source video/audio
					// use d.getContentType() to determine which or use
					// v.targetFile(dinfo, ext, conflict) to set name dynamically or
					// d.targetFile = new File("/Downloads/CustomName.mp3");
					// to set destinationTarget name manually.
					System.out.println("Download URL: " + d.getSource());
				}
			}

			v.download(user, stop, notify);
			for(VideoFileInfo videoFileInfo : videoinfo.getInfo()){
				videos.add(new File(path + videoFileInfo.targetFile.getName()));
			}
			return videos;
		} catch (DownloadInterruptedError e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<File> getVideosWithSameTitle(String path, String title) {
		File folder = new File(path);
		List<File> videos = new ArrayList<>();
		if (folder.isDirectory()) {
			File[] listOfFiles = folder.listFiles();
			if(listOfFiles == null){
				return videos;
			}
			for(File file : listOfFiles){
				if(file.getName().contains(title)){
					videos.add(file);
				}
			}
		}
		return videos;
	}
}
