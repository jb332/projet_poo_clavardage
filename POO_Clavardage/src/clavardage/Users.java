package clavardage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Users {
    private ArrayList<User> users;

    public Users() {
        users = new ArrayList<User>();
        //create users for testing
        users.add(new User("John", "192.168.1.1", "11:11:11:11:11:11"));
        users.add(new User("James", "192.168.1.2", "11:11:11:11:11:12"));
        users.add(new User("Jack", "192.168.1.3", "11:11:11:11:11:13"));
        users.add(new User("Johnson", "192.168.1.4", "11:11:11:11:11:14"));
        users.add(new User("Jackson", "192.168.1.5", "11:11:11:11:11:15"));
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getUserFromLogin(String login) {
        ArrayList<User> users = new ArrayList<User>();
        User foundUser = null;
        Iterator i = this.users.iterator();
        while (i.hasNext() && foundUser == null) {
            User currentUser = (User)(i.next());
            if(currentUser.getLogin().equals(login)) {
                foundUser = currentUser;
            }
        }
        return foundUser;
    }

    public User getUserFromIP(String ipAddress) {
        Iterator i = this.users.iterator();
        User foundUser = null;
        while (i.hasNext() && foundUser == null) {
            User currentUser = (User)(i.next());
            if(currentUser.getIpAddress().equals(ipAddress)) {
                foundUser = currentUser;
            }
        }
        return foundUser;
    }

    public ArrayList<String> getLogins() {
        ArrayList<String> logins = new ArrayList<String>();
        for(User currentUser: this.users) {
            logins.add(currentUser.getLogin());
        }
        return logins;
    }

    /*
    public ArrayList<UserButton> generateUserButtons() {
        ArrayList<UserButton> userButtons = new ArrayList<UserButton>();
        for(User currentUser : users) {
            userButtons.add(new UserButton(currentUser));
        }
        return userButtons;
    }
    */

    public ArrayList<JButton> generateUserButtons() {
        ArrayList<JButton> userButtons = new ArrayList<JButton>();
        for(User currentUser : users) {
            userButtons.add(new JButton(currentUser.getLogin()));
        }
        return userButtons;
    }

    public User getArbitraryUser() {
        return this.users.get(0);
    }

    public String toString() {
        String stringOut = "";
        for(User currentUser : this.users) {
            stringOut += currentUser + "\n\n";
        }
        return stringOut;
    }

    public void shutdownSockets() {
        for(User currentUser : this.users) {
            try {
                if(currentUser.socketExists()) {
                    currentUser.getSocket().close();
                }
            } catch(IOException e) {
                System.out.println("Failed to close socket :");
                System.out.println(e);
            }
        }
    }
}
