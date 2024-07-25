package utils.jsonutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import models.OnlinePlayer;
import models.Response;

/**
 * Utility class for sending JSON data to a server and receiving a response.
 */
public class JsonSender {

    private static final Gson gson = new Gson(); // Use a single Gson instance

    /**
     * Sends JSON data to the server and receives a response.
     *
     * @param jsonData the JSON data to send
     * @param serverAddress the server address
     * @param serverPort the server port
     * @return the Response object from the server
     */
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
                // Deserialize JSON to Response object
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

    public static ArrayList<OnlinePlayer> sendJsonAndReceivePlayersList(String jsonData, String serverAddress, int serverPort) {
        try (Socket socket = new Socket(serverAddress, serverPort);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            writer.write(jsonData + "\n");
            writer.flush();

            String jsonString = reader.readLine();
            System.out.println("Heree /n" + jsonString);
            if (jsonString != null) {
                JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
                JsonArray playersArray = jsonObject.getAsJsonArray("players");
                ArrayList<OnlinePlayer> playersList = new ArrayList<>();
                for (JsonElement element : playersArray) {
                    OnlinePlayer player = gson.fromJson(element, OnlinePlayer.class);
                    playersList.add(player);
                }

                return playersList;
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
