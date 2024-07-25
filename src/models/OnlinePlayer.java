/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import enumpackages.statusenum.EnumStatus.Status;

/**
 *
 * @author Mohammed
 */
public class OnlinePlayer {

    private int id;
    private int points;
    private String userName;
    private String password;
    private Status status;
    private String action;
    private String message; // For chat messages
// Getter and Setter methods

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public Status getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
