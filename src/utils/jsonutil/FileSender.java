/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.jsonutil;

/**
 *
 * @author Mohammed
 */
//4. Send File to Server
import java.io.*;
import java.net.Socket;

public class FileSender {

    public static void sendFile(String filePath, String serverAddress, int serverPort) {
        File file = new File(filePath);
        try (Socket socket = new Socket(serverAddress, serverPort);
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream os = socket.getOutputStream()) {

            byte[] buffer = new byte[(int) file.length()];
            bis.read(buffer, 0, buffer.length);
            os.write(buffer, 0, buffer.length);
            os.flush();

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException during file transfer: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
