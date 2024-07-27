package models;

import java.util.ArrayList;

public class InOnlineResponse {
    public boolean isDone;
    public String message;
    public ArrayList<OnlinePlayer> players;

    public InOnlineResponse(boolean isDone, String message, ArrayList<OnlinePlayer> players) {
        this.isDone = isDone;
        this.message = message;
        this.players = players;
    }

    // Getters and Setters
    public boolean getStatus() {
        return isDone;
    }

    public void setStatus(boolean isDone) {
        this.isDone = isDone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<OnlinePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<OnlinePlayer> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "InOnlineResponse{" +
                "isDone=" + isDone +
                ", message='" + message + '\'' +
                ", players=" + players +
                '}';
    }
}
