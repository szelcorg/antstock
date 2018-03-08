package org.szelc.app.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.szelc.app.antstock.settings.Settings;

/**
 * @author marcin.szelc
 */
public class FtpUtil {

    private static String server;
    private static int port;
    private static String user;
    private static String pass;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(Settings.PROPERTIES));
            server = properties.getProperty("ftp.host");
            port = Integer.valueOf(properties.getProperty("ftp.port"));
            user = properties.getProperty("ftp.user");
            pass = properties.getProperty("ftp.pass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public static void show() {

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return;
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("Could not login to the server");
                return;
            } else {
                System.out.println("LOGGED IN SERVER");
            }
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }
    }

    public static boolean[] uploadFile(List<String> srcList, List<String> dstList) {
        boolean result[] = new boolean[srcList.size()];
        FTPClient ftpClient = connect(server, port, user, pass);
        if (ftpClient == null) {
            System.out.println("Brak polaczania");
            return result;
        }
        for (int i = 0; i < srcList.size(); i++) {
            System.out.println("Try upload file number [" + i + "]");
            result[i] = uploadFile(ftpClient, srcList.get(i), dstList.get(i));
        }
        disconnect(ftpClient);
        return result;
    }

    public static boolean uploadFile(FTPClient ftpClient, String src, String dst) {
        boolean success = false;
        InputStream inputStream = null;
        try {
            File firstLocalFile = new File(src);
            inputStream = new FileInputStream(firstLocalFile);
            System.out.println("Start uploading first file from [" + src + "] to [" + dst + "]");
            boolean done = ftpClient.storeFile(dst, inputStream);
            inputStream.close();
            if (done) {
                success = true;
                System.out.println("The first file is uploaded successfully.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            success = false;
            ex.printStackTrace();
        }
        return success;
    }

    public static FTPClient connect(String host, int port, String user, String pass) {
        try {
            System.out.println("Try download");
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(host, port);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
            System.out.println("Connected");
            ftpClient.login(user, pass);
            ftpClient.setBufferSize(1024 * 1024);
            System.out.println("Logged");
            ftpClient.enterLocalPassiveMode();
            System.out.println("Enter passive mode");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient;
        } catch (IOException ex) {
            Logger.getLogger(FtpUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static void disconnect(FTPClient ftpClient) {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean[] downloadFile(List<String> srcList, List<String> dstList) {
        boolean result[] = new boolean[srcList.size()];
        FTPClient ftpClient = connect(server, port, user, pass);
        if (ftpClient == null) {
            System.out.println("Brak polaczania");
            return result;
        }
        for (int i = 0; i < srcList.size(); i++) {
            System.out.println("Try download file number [" + i + "]");
            result[i] = downloadFile(ftpClient, srcList.get(i), dstList.get(i));
        }
        disconnect(ftpClient);
        return result;
    }

    public static boolean downloadFile(FTPClient ftpClient, String src, String dst) {
        boolean success = false;
        OutputStream outputStream1 = null;
        try {
            outputStream1 = new BufferedOutputStream(new FileOutputStream(dst));
            System.out.println("Pobieranie pliku [" + src + "] do [" + dst + "]");

            success = ftpClient.retrieveFile(src, outputStream1);
            System.out.println("Pobrany plik [" + success + "]");
            outputStream1.close();

            if (success) {
                System.out.println("File #1 has been downloaded successfully.");
            } else {
                System.out.println("Failure download file");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (outputStream1 != null)
                try {
                    outputStream1.close();
                } catch (IOException ex) {
                    Logger.getLogger(FtpUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

        }
        return success;
    }
}
