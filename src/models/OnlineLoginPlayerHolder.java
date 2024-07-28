package models;

import java.util.List;
import static utils.jsonutil.JsonSender.receiveResponse;

public class OnlineLoginPlayerHolder {

    private OnlinePlayer player;
    private Thread serverListenerThread;
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
                        serverMessage = receiveResponse();
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

    public Response getServerMessage() {
        return serverMessage;
    }

    // Method to get the list of players from InOnlineResponse
    public synchronized List<OnlinePlayer> getPlayers() {
        return inOnlineResponse != null ? inOnlineResponse.getPlayers() : null;
    }
}
