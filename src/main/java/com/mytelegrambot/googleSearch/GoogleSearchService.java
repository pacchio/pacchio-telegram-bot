package com.mytelegrambot.googleSearch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import static com.mytelegrambot.ExceptionHelper.getExceptionStacktrace;

@Log4j2
public class GoogleSearchService {

	private static final String PROPERTIES_FILENAME = "google-api.properties";

	private Properties properties = new Properties();

	public GoogleSearchService(){
		setProperties();
	}

	private void setProperties(){
		try {
			InputStream in = GoogleSearchService.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);
		} catch (IOException e) {
			log.error(getExceptionStacktrace(e));
			System.exit(1);
		}
	}

	 public List<GResult> search(String chiaveDiRicerca)  throws MalformedURLException, URISyntaxException, IOException {
		 String key = properties.getProperty("google.apikey");
		 String cx  = properties.getProperty("google.custom-search-engine-id");
		 String fileType = properties.getProperty("google.search.fileType");
		 String searchType = properties.getProperty("google.search.searchType");
		 Integer numItems = Integer.parseInt(properties.getProperty("google.search.numItems"));
		 List<GResult> results = new ArrayList<>();
		 for(int start=1;start<numItems;start+=10){
			 URL url = new URL ("https://www.googleapis.com/customsearch/v1?key=" +key+ "&cx=" +cx+ "&q=" +chiaveDiRicerca+"&fileType="+fileType+"&searchType="+searchType+"&start="+start+"&alt=json");
			 log.info("Chiamata numero " + (start + 10 - 1) / 10 + " effettuata all'url: " + url.toString());
			 JSONObject jsonResponse = readJsonFromUrl(url);
			 List<GResult> tempResults = new Gson().fromJson(jsonResponse.getJSONArray("items").toString(),new TypeToken<List<GResult>>() {}.getType());
			 results.addAll(tempResults);
		 }

		 return results;
	}

	public Map<Integer, ImageInfo> creaListaRisultati (List<GResult> results){
	 	Map<Integer, ImageInfo> mappaRisultati = new HashMap<>();
		log.info("Risultati:");
		for(int i = 0 ; i < results.size() ; i++){
			ImageInfo imageInfo = new ImageInfo();
			String filename = results.get(i).getLink();
			imageInfo.setFilename(controlFilenameQuality(filename));
			imageInfo.setUrl(results.get(i).getLink());
			mappaRisultati.put(i, imageInfo);
			log.info("\t" + i + " - " + imageInfo.getFilename());
		}
		return mappaRisultati;
	}

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	private JSONObject readJsonFromUrl(URL url) throws IOException, JSONException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(  conn.getInputStream() , Charset.forName("UTF-8") ) );
			String jsonText = readAll(br);
			JSONObject json = new JSONObject(jsonText);
			return json;
		}
		catch (Exception e){
			throw new RuntimeException("Errore durante il download!");
		}
		finally{
			conn.disconnect();
		}
	}

	private String controlFilenameQuality(String filename) {
		String titolo = filename.substring(filename.lastIndexOf('/') + 1);
		if(titolo.contains("?")){
			titolo = titolo.split("\\?")[0];
		}
		return titolo;
	}
}
