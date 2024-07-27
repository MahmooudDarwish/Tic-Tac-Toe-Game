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
import com.google.gson.JsonSyntaxException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import models.InOnlineResponse;
import models.OnlinePlayer;
import models.Response;

public class JsonSender {

    private static final Gson gson = new Gson();
    static Socket socket;

    public static Response sendJsonAndReceiveResponse(String jsonData, String serverAddress, int serverPort, boolean sendCheckerSocket) throws IOException {
        try {
            // Connect to the server
            socket = new Socket(serverAddress, serverPort);

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.write(jsonData + "\n");

            writer.flush();

            // Read the response from the server
            String jsonString = reader.readLine();
            if (jsonString != null) {
                return gson.fromJson(jsonString, Response.class);
            } else {
                System.out.println("Received null response from the server.");
                return null;
            }
        } catch (JsonSyntaxException | IOException e) {
            System.out.println("IOException during JSON data transfer: " + e.getMessage());
            return null;
        }
    }

    public static Response receiveResponse(String serverAddress, int serverPort) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String jsonString = reader.readLine();
            if (jsonString != null) {
                return gson.fromJson(jsonString, Response.class);
            } else {
                System.out.println("Received null response from the server.");
                return null;
            }

        } catch (IOException e) {
            System.out.println("IOException during JSON data transfer: " + e.getMessage());
            return null;
        }
    }

    public static InOnlineResponse sendJsonAndReceivePlayersList(String jsonData, String serverAddress, int serverPort) {
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.write(jsonData + "\n");
            writer.flush();

            String jsonString = reader.readLine();
            if (jsonString != null) {
                JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
                JsonArray playersArray = jsonObject.getAsJsonArray("players");
                ArrayList<OnlinePlayer>  playersList = new ArrayList<>();
                for (JsonElement element : playersArray) {
                    OnlinePlayer player = gson.fromJson(element, OnlinePlayer.class);
                    playersList.add(player);
                }

                // Assuming InOnlineResponse has a constructor that takes a List<OnlinePlayer>
                return new InOnlineResponse(true, "Players received successfully", playersList);
            } else {
                System.out.println("Received null response from the server.");
                return new InOnlineResponse(false, "No response received from server", null);
            }

        } catch (IOException e) {
            System.out.println("IOException during JSON data transfer: " + e.getMessage());
            return new InOnlineResponse(false, "IOException occurred", null);
        }
    }
}