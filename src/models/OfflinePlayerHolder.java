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
public class OfflinePlayerHolder {

    private OfflinePlayer xPlayer;
    private OfflinePlayer oPlayer;

    private final static OfflinePlayerHolder INSTANCE = new OfflinePlayerHolder();

    private OfflinePlayerHolder() {
    }

    public static OfflinePlayerHolder getInstance() {
        return INSTANCE;
    }

    public void setXPlayer(OfflinePlayer xPlayer) {
        this.xPlayer = xPlayer;
    }

    public OfflinePlayer getXPlayer() {
        return this.xPlayer;
    }

    public void setOPlayer(OfflinePlayer oPlayer) {
        this.oPlayer = oPlayer;
    }

    public OfflinePlayer getOPlayer() {
        return this.oPlayer;
    }

    public void clearOPlayer() {
        this.oPlayer = null;
    }

    public void clearXPlayer() {
        this.xPlayer = null;
    }
}
