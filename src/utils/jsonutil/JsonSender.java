package utils.jsonutil;

import java.io.BufferedReader;
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
import models.InOnlineResponse;
import models.OnlinePlayer;
import models.Response;
import utils.constants.AppConstants;

public class JsonSender {

    private static final Gson gson = new Gson();
    static Socket socket;

    public static void init() throws IOException {
        socket = new Socket(AppConstants.getServerIp(), 5006);

    }

    public static Response sendJsonAndReceiveResponse(String jsonData) throws IOException {
        try {

            // Socket rescieveRespsone = new Socket(AppConstants.getServerIp(), 5006);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.write(jsonData + "\n");

            writer.flush();

            // Read the response from the server
            String jsonString = reader.readLine();

            System.out.println(jsonString);

            if (jsonString != null) {
                return gson.fromJson(jsonString, Response.class
                );
            } else {
                System.out.println("Received null response from the server.");
                return null;
            }
        } catch (JsonSyntaxException | IOException e) {
            System.out.println("IOException during JSON data transfer: " + e.getMessage());
            return null;
        }
    }

    public static Response receiveResponse() {
        try {
            System.out.println("here port ->>" + socket.getLocalPort());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String jsonString = reader.readLine();
            System.out.println("recieved message" + jsonString);
            if (jsonString != null) {
                return gson.fromJson(jsonString, Response.class
                );
            } else {
                System.out.println("Received null response from the server.");
                return null;
            }

        } catch (IOException e) {
            System.out.println("IOException during JSON data transfer: " + e.getMessage());
            return null;
        }
    }

    public static InOnlineResponse sendJsonAndReceivePlayersList(String jsonData) {
        try {
            Socket socketList = new Socket(AppConstants.getServerIp(), 5006);

            PrintWriter writer = new PrintWriter(socketList.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socketList.getInputStream()));

            writer.write(jsonData + "\n");
            writer.flush();
            System.out.println("get players" + jsonData);
            String jsonString = reader.readLine();

            if (jsonString != null) {
                JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class
                );
                System.out.println("players response " + jsonObject);

                JsonArray playersArray = jsonObject.getAsJsonArray("players");
                ArrayList<OnlinePlayer> playersList = new ArrayList<>();

                for (JsonElement element : playersArray) {
                    OnlinePlayer player = gson.fromJson(element, OnlinePlayer.class
                    );
                    playersList.add(player);
                }

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
