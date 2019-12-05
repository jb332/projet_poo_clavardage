package clavardage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class History {
    ArrayList<UserData> history;

    /*
    public History(ArrayList<UserData> usersData) {
        this.history = usersData;
    }
    */

    public History() {
        this.history = new ArrayList<UserData>();
    }

    public void addUser(User user) {
        this.history.add(new UserData(user));
    }

    public void addUserData(UserData userData) {
        this.history.add(userData);
    }

    public void removeUserData(UserData userData) {
        this.history.remove(userData);
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
        Iterator i = this.history.iterator();
        while (i.hasNext()) {
            UserData currentUserData = (UserData)(i.next());
            User currentUser = currentUserData.getUser();
            users.add(currentUser);
        }
        return users;
    }
    
    public ArrayList<String> getLogins() {
    	ArrayList<String> logins = new ArrayList<String>();
    	for(UserData ud: this.history) {
    		logins.add(ud.getUser().getLogin());
    	}
    	return logins;
    }
}
