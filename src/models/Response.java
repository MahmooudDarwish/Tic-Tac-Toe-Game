/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Mohammed
 */

public class Response {
    private boolean isDone;
    private String message;
    private Object player;
       
    // Default constructor
    public Response() {}

    // Parameterized constructor
    public Response(boolean success, String message, Object data) {
        this.isDone = success;
        this.message = message;
        this.player = player;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return isDone;
    }

    public void setSuccess(boolean success) {
        this.isDone = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return player;
    }

    public void setData(Object data) {
        this.player = data;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "isDone=" + isDone +
                ", message='" + message + '\'' +
                ", player=" + player +
                '}';
    }
}