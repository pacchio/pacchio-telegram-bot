package com.mytelegrambot.raspberry;

import com.mytelegrambot.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MoneyHoneyManager {

    private final static String config_start = "exports.config = {\n"+
            "    seleniumAddress: 'http://localhost:4444/wd/hub',\n"+
            "    specs: ['../**/*.spec.js'],\n" +
            "    multiCapabilities: [\n";

    private final static String config_capability_start = "        {\n" +
            "            'browserName': 'chrome',\n" +
            "            chromeOptions: {\n" +
            "                args: [\"--incognito\"";

    private final static String config_capability_end = "]\n" +
            "            }\n" +
            "        },\n";

    private final static String config_end = "    ],\n" +
            "};";

    public void run() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("taskkill /IM cmd.exe");
        Thread.sleep(3000);
        Runtime.getRuntime().exec("taskkill /IM chrome.exe /F");
        Thread.sleep(2000);
        Runtime.getRuntime().exec("taskkill /IM chromedriver.exe /F");
        Thread.sleep(2000);

//        ProxyListService proxyListService = new ProxyListService();
//        List<Proxy> proxyList = proxyListService.getProxyListOnlyHttps(URL_ONLY_USA);
//        List<Proxy> proxyListLimited = proxyListService.getListLimitedTo(proxyList, 5);
//        writeFile(proxyListLimited, 5);

        Runtime.getRuntime().exec("cmd /c start \"\" " + Constants.MONEYHONEY_PATH + "start-selenium-server.bat");
        Thread.sleep(3000);
        Runtime.getRuntime().exec("cmd /c start \"\" " + Constants.MONEYHONEY_PATH + "start-test.bat");
        Thread.sleep(500);
        Runtime.getRuntime().exec("cmd /c start \"\" " + Constants.MONEYHONEY_PATH + "delete-local-temp.bat");
    }

    public void writeFile(List<Proxy> proxyList, int numeroConfigurazioniNormali) throws IOException {
        StringBuilder fileContent = new StringBuilder();

        fileContent.append(config_start);

        for(Proxy proxy : proxyList) {
            fileContent
                    .append(config_capability_start)
                    .append(", \"--proxy-server=http://")
                    .append(proxy.getHost())
                    .append(":")
                    .append(proxy.getPort())
                    .append("\"")
                    .append(config_capability_end);
        }
        for(int i = 0; i < numeroConfigurazioniNormali; i++) {
            fileContent
                    .append(config_capability_start)
                    .append(config_capability_end);
        }

        fileContent.append(config_end);

        FileWriter fileWriter = new FileWriter(Constants.MONEYHONEY_PATH  + "config\\protractor.conf.js");
        fileWriter.write(fileContent.toString());
        fileWriter.close();
    }

}
