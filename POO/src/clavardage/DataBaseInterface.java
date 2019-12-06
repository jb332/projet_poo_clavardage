package clavardage;

import java.util.ArrayList;

public class DataBaseInterface {

    private Clavardage chat;

    public DataBaseInterface(Clavardage chat) {
        this.chat = chat;
    }

    public void storeMessage(Message message) {
        // TODO Auto-generated method stub

    }
    
    public void storeUser(User user) {
    	
    }

    public void getUsers(ArrayList<User> connectedUsers) {

    }
}
