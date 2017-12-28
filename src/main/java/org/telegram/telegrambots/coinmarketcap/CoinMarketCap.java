package org.telegram.telegrambots.coinmarketcap;

import javafx.util.converter.BigDecimalStringConverter;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.telegram.telegrambots.Constants.MESSAGE_COINMARKETCAP_FAILED;

public class CoinMarketCap {

	public static String ONE_DOLLAR_IN_EURO = "0.842452886";
	final Logger logger = Logger.getLogger(CoinMarketCap.class);

	public String disallinamenti(){
		List<Coin> coins = new ArrayList<Coin>();
		try {
			for (int i = 1; i < 5; i++) {
				coins.addAll(getCoints(i));
			}

			Collections.sort(coins, (o1, o2) -> o1.getRapporto3().subtract(o2.getRapporto3()).signum());

			String results = "";

			for (Coin c : coins) {
				if (c.getMarketCap().compareTo(new BigDecimal("400000000")) == 1) {
					results += c.getSymbol() + " \t " + c.getRapporto3() + " \n";
				}
			}
			return results;
		} catch (Exception e){
			logger.error(e);
			return MESSAGE_COINMARKETCAP_FAILED;
		}
	}

	private List<Coin> getCoints(int page) throws IOException {
		List<Coin> coins = new ArrayList<Coin>();

		Connection.Response rs = Jsoup.connect("https://coinmarketcap.com/" + page).execute();
		Elements trs = rs.parse().select("#currencies tr");

		for (Element tr : trs) {
			String symbol = tr.select(".currency-symbol").text();
			BigDecimal price = parseDecimal(tr.select(".price").text());
			BigDecimal circulatingSupply = parseDecimal(
					tr.select(".circulating-supply").text());
			BigDecimal marketCap = parseDecimal(
					tr.select(".market-cap").text());

			if (circulatingSupply != null) {
				Coin coin = new Coin();
				coin.setCirculatingSupply(circulatingSupply);
				coin.setPrice(price);
				coin.setMarketCap(marketCap);
				coin.setSymbol(symbol);
				coin.setRapporto1(circulatingSupply.divide(price, 2, RoundingMode.HALF_UP));
				coin.setRapporto2(marketCap.divide(price, 2, RoundingMode.HALF_UP));
				coin.setRapporto3(coin.getRapporto1().divide(coin.getRapporto2(), 2, RoundingMode.HALF_UP));

				coins.add(coin);
			}
		}

		return coins;
	}

	public String info(){
		List<Coin> coins = new ArrayList<Coin>();
		try {
		    ONE_DOLLAR_IN_EURO = oneDollarValueInEuro();
			coins.addAll(getCoints(1));

			String results = "";
			String format = "%1$-20s%2$-10s\n";

			for (Coin c : coins) {
				results += String.format(format, c.getSymbol(), getValueInEuro(c) + " €");
			}
			return results;
		} catch (Exception e){
			logger.error(e);
			return MESSAGE_COINMARKETCAP_FAILED;
		}
	}

	private BigDecimal getValueInEuro(Coin c) {
        return c.getPrice().multiply(new BigDecimal(ONE_DOLLAR_IN_EURO)).setScale(4, RoundingMode.CEILING);
	}

    private String oneDollarValueInEuro() {
        try {
            Connection.Response rs = Jsoup.connect("http://www.x-rates.com/calculator/").execute();
            Document doc = rs.parse();
            Element els = doc.getElementsByClass("ccOutputRslt").first();
            return els.text().replace("EUR", "").trim();
        } catch (IOException e) {
            logger.error(e);
            return MESSAGE_COINMARKETCAP_FAILED;
        }
	}

    private static BigDecimal parseDecimal(String text) {
		return new BigDecimalStringConverter()
				.fromString(onlyNumbers(text));
	}

	private static String onlyNumbers(String text) {
		String result = new String();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (Character.isDigit(c) || c == '.') {
				result += c;
			}
		}

		return result.trim();
	}

}