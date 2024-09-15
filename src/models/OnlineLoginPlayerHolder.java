package models;

import java.util.List;

public class OnlineLoginPlayerHolder {
    private Player player;
    private Response serverMessage;
    private final static OnlineLoginPlayerHolder INSTANCE = new OnlineLoginPlayerHolder();

    private OnlineLoginPlayerHolder() {
    }

    public static OnlineLoginPlayerHolder getInstance() {
        return INSTANCE;
    }

    public void setPlayer(Player xPlayer) {
        this.player = xPlayer;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void clearPlayer() {
        this.player = null;
    }

    public void clearServerMessage() {
        this.serverMessage = null;
    }

 
}
