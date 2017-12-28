package com.mytelegrambot.youtubeSearch;

import java.util.HashMap;
import java.util.Map;

public class MySingletonMap {
	private Map<Integer, String> map = new HashMap<Integer , String>();

	private static MySingletonMap instance = null;

	private MySingletonMap() {}

	public Map<Integer, String> getMap() {
		return map;
	}

	public static MySingletonMap getInstance() {
		if (instance == null)
			instance = new MySingletonMap();
		return instance;

	}

	public void put(Integer index, String url) {
		map.put(index,url);
	}
}

