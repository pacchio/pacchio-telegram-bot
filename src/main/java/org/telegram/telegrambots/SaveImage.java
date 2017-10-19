package org.telegram.telegrambots;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SaveImage {

    final static Logger logger = Logger.getLogger(SaveImage.class);

    public static void main(String[] args) throws MalformedURLException {
        download();
    }

    private static void download() throws MalformedURLException {
        URL url = new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTNrjGPA7-an7oZjH7X3crcDS8xF-6R2mqkCJ9162zXyR23s8h5v8QO0T1");
        String destinazionFolder = "D:\\Users\\arx50054\\Desktop";
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String filename = "miele_biologico.jpg";
//            String filename = url.getFile();
//            filename = filename.substring(filename.lastIndexOf('/') + 1);
            FileOutputStream outputStream = new FileOutputStream(destinazionFolder + File.separator + filename);
            InputStream inputStream = connection.getInputStream();

            int read = -1;
            byte[] buffer = new byte[4096];

            while ((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, read);
                logger.info("File is downloading...");
            }

            inputStream.close();
            outputStream.close();

            logger.info("Download completed !");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

