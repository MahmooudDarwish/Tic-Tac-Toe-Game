package models;

import enumpackages.statusenum.EnumStatus.Status;

/**
 * Represents an online player with extended response details.
 */
public class OnlinePlayer extends Response {
    private int id;
    private int points;
    private String userName;
    private String password;
    private Status status;
    private String action;

    public OnlinePlayer() {
        super(); // Call parent constructor
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

 

    @Override
    public String toString() {
        return "OnlinePlayer{" +
                "id=" + id +
                ", points=" + points +
                ", userName='" + userName + '\'' +
                ", status=" + status +
                ", action='" + action + '\'' +
                ", message='" + getMessage() + '\'' +
                "} " + super.toString();
    }
}
