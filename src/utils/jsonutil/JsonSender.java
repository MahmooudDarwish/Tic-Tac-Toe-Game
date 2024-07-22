/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.jsonutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import com.google.gson.Gson;
import models.Response;

/**
 *
 * @author Mohammed
 */
public class JsonSender {
    static Response response;
    private static Gson gson = new Gson();
    public static Response sendJsonAndReceiveResponse(String jsonData, String serverAddress, int serverPort) {
        try (Socket socket = new Socket(serverAddress, serverPort);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send JSON data to the server
            writer.write(jsonData + "\n"); // Add a newline character to signify end of message
            writer.flush();

            // Read the server's response
            String jsonString = reader.readLine(); // Read JSON string from the server
            if (jsonString != null) {
                // Deserialize JSON to ResponseDTO object
                return gson.fromJson(jsonString, Response.class);
            } else {
                System.err.println("Received null response from the server.");
                return null;
            }

        } catch (IOException e) {
            System.err.println("IOException during JSON data transfer: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
