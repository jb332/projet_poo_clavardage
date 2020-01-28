package clavardage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

public class Users {
    private ArrayList<User> users;

    public Users() {
        users = new ArrayList<User>();
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addUsers(ArrayList<User> users) {
        for(User currentUser : users) {
            this.users.add(currentUser);
        }
    }

    public User getUserFromLogin(String login) {
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

    public User getUserFromMacAddress(String macAddress) {
        User foundUser = null;
        Iterator i = this.users.iterator();
        while(i.hasNext() && foundUser == null) {
            User currentUser = (User)(i.next());
            if(currentUser.getMacAddress().equals(macAddress)) {
                foundUser = currentUser;
            }
        }
        return foundUser;
    }

    public User getUserFromIP(InetAddress ipAddress) throws IllegalArgumentException {
        User foundUser = null;
        Iterator i = this.users.iterator();
        while (i.hasNext() && foundUser == null) {
            User currentUser = (User)(i.next());
            if(currentUser.isConnected() && currentUser.getIpAddress().equals(ipAddress)) {
                foundUser = currentUser;
            }
        }
        if(foundUser == null) {
            throw new IllegalArgumentException("Error : no user having the IP address \"" + ipAddress + "\" was found.");
        } else {
            return foundUser;
        }
    }

    public boolean isLoginAvailableAmongOtherUsers(String login, String macAddress) {
        boolean loginAvailable = true;
        Iterator i = this.users.iterator();
        while(i.hasNext() && loginAvailable) {
            User currentUser = (User)(i.next());
            System.out.println("login : " + currentUser.getLogin());
            if(currentUser.getLogin().equals(login) && currentUser.isConnected() && currentUser.getMacAddress() != macAddress) {
                loginAvailable = false;
            }
        }
        return loginAvailable;
    }

    public ArrayList<User> getList() {
        return this.users;
    }

    public User getArbitraryUser() {
        if(this.users.size() > 0) {
            return this.users.get(0);
        } else {
            return null;
        }
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
