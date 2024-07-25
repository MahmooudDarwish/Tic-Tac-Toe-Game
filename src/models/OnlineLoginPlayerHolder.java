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
public class OnlineLoginPlayerHolder {

    private OnlinePlayer player;

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
}
