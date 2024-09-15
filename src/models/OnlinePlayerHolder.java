/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Mahmoud
 */
public class OnlinePlayerHolder {

    private Player xPlayer;
    private Player oPlayer;

    private final static OnlinePlayerHolder INSTANCE = new OnlinePlayerHolder();

    private OnlinePlayerHolder() {
    }

    public static OnlinePlayerHolder getInstance() {
        return INSTANCE;
    }

    public void setXPlayer(Player xPlayer) {
        this.xPlayer = xPlayer;
    }

    public Player getXPlayer() {
        return this.xPlayer;
    }

    public void setOPlayer(Player oPlayer) {
        this.oPlayer = oPlayer;
    }

    public Player getOPlayer() {
        return this.oPlayer;
    }

    public void clearOPlayer() {
        this.oPlayer = null;
    }

    public void clearXPlayer() {
        this.xPlayer = null;
    }
}
