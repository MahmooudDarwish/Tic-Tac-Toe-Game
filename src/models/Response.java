package models;

public class Response {
    private boolean isDone;
    private String message;
    private OnlinePlayer player;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OnlinePlayer getPlayer() {
        return player;
    }

    public void setPlayer(OnlinePlayer player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "Response{" +
                "isDone=" + isDone +
                ", message='" + message + '\'' +
                ", player=" + player +
                '}';
    }
}
