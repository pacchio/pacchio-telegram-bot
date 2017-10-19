package org.telegram.telegrambots.googleSearch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GoogleSearchService {

	final Logger logger = Logger.getLogger(GoogleSearchService.class);

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
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
					+ " : " + e.getMessage());
			System.exit(1);
		}
	}

	 public List<GResult> search(String chiaveDiRicerca)  throws MalformedURLException, URISyntaxException, IOException {
		String key = properties.getProperty("google.apikey");
		String cx  = properties.getProperty("google.custom-search-engine-id");
		String qry = chiaveDiRicerca;
		String fileType = "png,jpg,jpeg";
		String searchType = "image";
		URL url = new URL ("https://www.googleapis.com/customsearch/v1?key=" +key+ "&cx=" +cx+ "&q=" +qry+"&fileType="+fileType+"&searchType="+searchType+"&alt=json");
		logger.info("Chiamata effettuata all'url: " + url.toString());
		JSONObject jsonResponse = readJsonFromUrl(url);
		List<GResult> results = new Gson().fromJson(jsonResponse.getJSONArray("items").toString(),new TypeToken<List<GResult>>() {}.getType());
		return results;
	}

	public Map<Integer, ImageInfo> creaListaRisultati (List<GResult> results){
	 	Map<Integer, ImageInfo> mappaRisultati = new HashMap<>();
		logger.info("Risultati:");
		for(int i = 0 ; i < results.size() ; i++){
			ImageInfo imageInfo = new ImageInfo();
			String filename = results.get(i).getLink();
			imageInfo.setFilename(controlFilenameQuality(filename));
			imageInfo.setUrl(results.get(i).getImage().getThumbnailLink());
			mappaRisultati.put(i, imageInfo);
			logger.info("\t" + i + " - " + imageInfo.getFilename());
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
