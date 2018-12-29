package com.mytelegrambot.raspberry;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ProxyListService {

	final static String URL_ONLY_HTTPS = "https://www.sslproxies.org/";
	final static String URL_ONLY_USA = "https://www.us-proxy.org/";
	final static String URL_ONLY_UK = "https://free-proxy-list.net/uk-proxy.html";
	final static String URL_ALL = "https://free-proxy-list.net/";


	public static void main(String[] args) {
		ProxyListService proxyListService = new ProxyListService();
		try {
			List<Proxy> proxyList = proxyListService.getListLimitedTo(proxyListService.getProxyListOnlyHttps(URL_ONLY_USA), 5);
			for(Proxy proxy : proxyList) {
				System.out.println("Host: " + proxy.getHost() + "\t\t\tPort: " + proxy.getPort() + "\t\t\tCountry: " + proxy.getCountry());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Proxy> getProxyList(String url) throws IOException {
		List<Proxy> proxyList = new ArrayList<>();

		Connection.Response rs = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
				.execute();
		Elements trs = rs.parse().select("#proxylisttable tr");

		for (Element tr : trs) {
			if(tr.child(0).text().matches("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b")) {
				Proxy proxy = new Proxy();
				proxy.setHost(tr.child(0).text());
				proxy.setPort(tr.child(1).text());
				proxy.setCode(tr.child(2).text());
				proxy.setCountry(tr.child(3).text());
				proxy.setAnonimity(tr.child(4).text());
				proxy.setGoogle(tr.child(5).text());
				proxy.setHttps(tr.child(6).text());
				proxy.setLastChecked(tr.child(7).text());
				proxyList.add(proxy);
			}
		}

		return proxyList;
	}

	List<Proxy> getProxyListOnlyHttps(String url) throws IOException {
		List<Proxy> proxyListOnlyHttps = new ArrayList<>();
		List<Proxy> proxyList = getProxyList(url);
		for(Proxy proxy : proxyList) {
			if(proxy.getHttps().equals("yes")) {
				proxyListOnlyHttps.add(proxy);
			}
		}
		return proxyListOnlyHttps;
	}

	List<Proxy> getListLimitedTo(List<Proxy> proxyList, int number) {
		List<Proxy> proxyListLimited = new ArrayList<>();
		while (proxyListLimited.size() != number) {
			Proxy proxy = proxyList.get((int) (Math.random() * proxyList.size()));
			if(new CheckProxy().checkProxie(proxy.getHost(), Integer.parseInt(proxy.getPort()))) {
				proxyListLimited.add(proxy);
			}
		}
		return proxyListLimited;
	}



}