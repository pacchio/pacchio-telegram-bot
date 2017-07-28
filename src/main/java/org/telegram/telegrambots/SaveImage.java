package org.telegram.telegrambots;

import javafx.scene.image.Image;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.glassfish.grizzly.streams.StreamReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SaveImage {

    public static void main(String[] args) throws MalformedURLException {
        download();
    }

    private static void download() throws MalformedURLException {
        URL url = new URL("https://www.google.it/images/icons/material/system/1x/email_grey600_24dp.png");
        String destinazionFolder = "D:\\Users\\arx50054\\Desktop\\Foto\\Random";
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String filename = url.getFile();
            filename = filename.substring(filename.lastIndexOf('/') + 1);
            FileOutputStream outputStream = new FileOutputStream(destinazionFolder + File.separator + filename);
            InputStream inputStream = connection.getInputStream();

            int read = -1;
            byte[] buffer = new byte[4096];

            while ((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, read);
                System.out.println("File is downloading...");
            }

            inputStream.close();
            outputStream.close();

            System.out.println("Download completed !");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

