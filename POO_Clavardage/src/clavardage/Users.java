package clavardage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class representing a set of users
 */
public class Users {
    private ArrayList<User> users;

    /**
     * Constructor
     */
    public Users() {
        users = new ArrayList<User>();
    }

    /**
     * Add a user
     * @param user the user you want to add
     */
    public void addUser(User user) {
        this.users.add(user);
    }

    /**
     * Add users
     * @param users the list of users you want to add
     */
    public void addUsers(ArrayList<User> users) {
        for(User currentUser : users) {
            this.users.add(currentUser);
        }
    }

    /**
     * Get a user from his login
     * @param login the login you want to find a user from
     * @return the user found or null if no user was found
     */
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

    /**
     * Get a user from his MAC address
     * @param macAddress the MAC you want to find a user from
     * @return the user found or null if no user was found
     */
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

    /**
     * Get a connected user from his user IP address. If no connected user has this IP, null is returned
     * @param ipAddress the InetAddress you want to find a user from
     * @return the user found
     * @throws IllegalArgumentException raised when no user was found
     */
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

    /**
     * Tell if a login is available by checking the connected users logins. Be careful, this method does not check if a login is different from your own login.
     * @param login the login you want to check availability of
     * @param macAddress the macAddress associated with the login parameter (allow the method to ignore equality with his former login for the user requesting a login)
     * @return true if a login is available among connected users (except myself), or false otherwise
     */
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

    /**
     * Get a list of users
     * @return a list of users
     */
    public ArrayList<User> getList() {
        return this.users;
    }

    /**
     * Get an arbitrary user among all users (connected or not).
     * @return an arbitrary user
     */
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

    /**
     * Close all opened sockets associated with users. Note : This method is called on exit by a shutdown hook.
     */
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
