package org.szelc.app.antstock.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author marcin@szelc.org
 */
public class FileUtil {

    private static final Logger log = Logger.getLogger(FileUtil.class.getName());

    public static boolean downloadFileFromURL(String fileURL, String fileOutput) {
        log.info("DownloadFileFromUrl fileUrl [" + fileURL + "] fileOutput [" + (new File(fileOutput)).getAbsolutePath() + "]");
        long downloadedBytes = 0;
        int downloadedPercent = 0;
        int fileSize;
        int bufferSize = 2048;
        try {
            URL url = new URL(fileURL);
            URLConnection conn = url.openConnection();
            log.info("Con length +" + conn.getContentLength());
            fileSize = conn.getContentLength();
            InputStream reader;
            log.info("Opening stream ..");
            reader = url.openStream();
            log.info("Opened stream");
            try (FileOutputStream writer = new FileOutputStream(fileOutput)) {
                File file = new File(fileOutput);

                //log.log(Level.INFO, "FD {0}", file.getAbsolutePath());
                byte[] buffer = new byte[bufferSize];
                //int totalBytesRead = 0;
                int bytesRead;
                int total = 0;
                int totalPercent = 0;
                long timeStart = System.currentTimeMillis();
                log.info("Starting reading ...");
                while ((bytesRead = reader.read(buffer)) != -1 ) {
                    log.info("Readed bytes ["+bytesRead+"]");
                    //System.out.println("Read bytes ["+bytesRead+"]");
                    total += bytesRead;
//                    log.info("Total read ["+(total/1024));
                    int percent = 100 * total / conn.getContentLength();
                    if (percent > totalPercent) {
                        totalPercent = percent;

                        long timeCurr = System.currentTimeMillis();
                        int elapsedTime = (int) (timeCurr - timeStart);
                        int przewidywanyCalyCzas = elapsedTime * 100 / totalPercent;
                        int pozostalo = przewidywanyCalyCzas - elapsedTime;
                        log.info("Readed " + totalPercent + "%, Pozostalo sekund " + pozostalo / 1000);
                    }
                    downloadedBytes += bytesRead;
                    log.info("Downloaded KBytes "+downloadedBytes / 1024);                    
                    writer.write(buffer, 0, bytesRead);
                }
                log.info("Finished");
                log.info("Done " + (downloadedBytes) + " bytes read");
            }
            System.out.println("Close");
            reader.close();
            return true;
        } catch (MalformedURLException e) {
            log.error("Blad pobierania pliku " + e.getMessage());
              log.error(e);
            System.exit(0);
        } catch (IOException e) {
           
            log.error("Blad pobierania pliku " + e.getMessage());
              log.error(e);
            System.exit(0);
        }
      
        return false;
    }

    public static boolean unpackZipFile(String fileSrc, String pathDst) {
        log.info("Unzipping file ... src [" + fileSrc + "] dst [" + pathDst + "]");
        Enumeration entries;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(fileSrc);
            entries = zipFile.entries();
            File file = new File(pathDst);
            if (!file.exists()) {
                //log.info("Create directory path [" + pathDst + "] filename [" + file.getName() + "]");
                file.mkdir();
            }
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    log.info("Extracting directory: " + entry.getName());
                    (new File(pathDst + entry.getName())).mkdir();
                    continue;
                }
                //log.info("Extracting file: " + pathDst + entry.getName());
                copyInputStream(zipFile.getInputStream(entry),
                        new BufferedOutputStream(new FileOutputStream(pathDst + entry.getName())));
            }
            zipFile.close();
        } catch (IOException e) {
            log.error("Error during unzipping file MSG [" + e.getMessage() + "]");
             log.error(e);
            System.exit(0);
            return false;
        }
        return true;
    }

    private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }
}
