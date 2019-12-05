package clavardage;

import java.util.ArrayList;

public class UserData {
    User user;
    ArrayList<Message> messages;

    public UserData(User user, ArrayList<Message> messages) {
        this.user = user;
        this.messages = messages;
    }

    public UserData(User user) {
        this.user = user;
        this.messages = new ArrayList<Message>();
    }

    public User getUser() {
        return this.user;
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }
}
