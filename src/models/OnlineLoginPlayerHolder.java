package models;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;
import static utils.jsonutil.JsonSender.receiveResponse;

public class OnlineLoginPlayerHolder {

    private OnlinePlayer player;
    private Thread serverListenerThread;
    private Thread getOnlinePlayerThread;
    private volatile boolean running;
    private Response serverMessage;
    private InOnlineResponse inOnlineResponse;
    private final static OnlineLoginPlayerHolder INSTANCE = new OnlineLoginPlayerHolder();

    private OnlineLoginPlayerHolder() {
    }

    public static OnlineLoginPlayerHolder getInstance() {
        return INSTANCE;
    }

    public void setPlayer(OnlinePlayer xPlayer) {
        this.player = xPlayer;
    }

    public OnlinePlayer getPlayer() {
        return this.player;
    }

    public void clearPlayer() {
        this.player = null;
    }

    public void clearServerMessage() {
        this.serverMessage = null;
    }

    public synchronized void startServerListener() {
        if (serverListenerThread == null || !serverListenerThread.isAlive()) {
            running = true;
            serverListenerThread = new Thread(() -> {
                while (running) {
                    try {
                        System.out.println("Checking server status...");
                        serverMessage = receiveResponse(AppConstants.getServerIp(), 5006);
                        System.out.println("Received response: " + serverMessage);

                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread was interrupted");
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println("An error occurred");
                        e.printStackTrace();
                    }
                }

            });
            serverListenerThread.start();
        } else {
            System.out.println("Thread is already running or not alive.");
        }
    }

    public synchronized void stopServerListener() {
        System.out.println("Stop server listener");
        if (serverListenerThread != null && serverListenerThread.isAlive()) {
            running = false;
            serverListenerThread.stop();
            cleanup();
        }
    }

    private void cleanup() {
        System.out.println("Clean up");
        serverListenerThread = null;
    }
/*
    public synchronized void startGetOnlinePlayerThread() {
        if (getOnlinePlayerThread == null || !getOnlinePlayerThread.isAlive()) {
            getOnlinePlayerThread = new Thread(() -> {
                try {
                    System.out.println("Starting get online player thread...");

                    String jsonRequest = "{\"action\":\"gamelobby\"}";
                    System.out.println("Sending JSON: " + jsonRequest);

                    // Receiving the response and storing it in inOnlineResponse
                    inOnlineResponse = JsonSender.sendJsonAndReceivePlayersList(jsonRequest, AppConstants.getServerIp(), 5006);
                    System.out.println("Players received: " + inOnlineResponse);

                    Response response = receiveResponse(AppConstants.getServerIp(), 5006); // Assuming port 5006 for this request
                    if (response != null) {
                        System.out.println("Received response for online players: " + response);
                    } else {
                        System.out.println("No response received for online players.");
                    }
                } catch (Exception e) {
                    System.out.println("An error occurred while getting online players");
                    e.printStackTrace();
                } finally {
                    System.out.println("Get online player thread finished.");
                }
            });

            System.out.println("Thread state before start: " + getOnlinePlayerThread.getState());
            getOnlinePlayerThread.start();
            System.out.println("Thread state after start: " + getOnlinePlayerThread.getState());
        } else {
            System.out.println("Get online player thread is already running or not alive.");
        }
    }

    public synchronized void stopGetOnlinePlayerThread() {
        System.out.println("Stopping get online player...");
        if (getOnlinePlayerThread != null && getOnlinePlayerThread.isAlive()) {
            getOnlinePlayerThread.interrupt(); // Safely interrupt the thread
            cleanupgetOnlinePlayerThread();
        } else {
            System.out.println("Get online player thread is not running.");
        }
    }

    private void cleanupgetOnlinePlayerThread() {
        System.out.println("Clean up");
        getOnlinePlayerThread = null;
    }
*/
    public Response getServerMessage() {
        return serverMessage;
    }

    // Method to get the list of players from InOnlineResponse
    public synchronized List<OnlinePlayer> getPlayers() {
        return inOnlineResponse != null ? inOnlineResponse.getPlayers() : null;
    }
}
