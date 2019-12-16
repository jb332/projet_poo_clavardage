package clavardage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class History {
    ArrayList<UserData> history;

    public History(ArrayList<UserData> usersData) {
        this.history = usersData;
    }

    public History() {
        this.history = new ArrayList<UserData>();

        //TEST
        ArrayList<Message> messages1 = new ArrayList<Message>();
        messages1.add(new Message("Salut John !", MessageWay.RECEIVED));
        messages1.add(new Message("Salut Dimitri !", MessageWay.SENT));
        messages1.add(new Message("Comment ça va ?", MessageWay.RECEIVED));
        messages1.add(new Message("Bien et toi ?", MessageWay.SENT));
        messages1.add(new Message("Bien.", MessageWay.RECEIVED));
        this.history.add(new UserData(new User("Dimitri"), messages1));

        ArrayList<Message> messages2 = new ArrayList<Message>();
        messages2.add(new Message("Salut John !", MessageWay.RECEIVED));
        messages2.add(new Message("Salut Raoul !", MessageWay.SENT));
        messages2.add(new Message("Comment ça va ?", MessageWay.RECEIVED));
        messages2.add(new Message("Bien et toi ?", MessageWay.SENT));
        messages2.add(new Message("Bien.", MessageWay.RECEIVED));
        this.history.add(new UserData(new User("Raoul"), messages2));

        ArrayList<Message> messages3 = new ArrayList<Message>();
        messages3.add(new Message("Salut John !", MessageWay.RECEIVED));
        messages3.add(new Message("Salut Tony !", MessageWay.SENT));
        messages3.add(new Message("Comment ça va ?", MessageWay.RECEIVED));
        messages3.add(new Message("Bien et toi ?", MessageWay.SENT));
        messages3.add(new Message("Bien.", MessageWay.RECEIVED));
        this.history.add(new UserData(new User("Tony"), messages3));
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

    public User getUserFromLogin(String login) {
        ArrayList<User> users = new ArrayList<User>();
        User foundUser = null;
        Iterator i = this.history.iterator();
        while (i.hasNext() && foundUser == null) {
            UserData currentUserData = (UserData)(i.next());
            User currentUser = currentUserData.getUser();
            if(currentUser.getLogin().equals(login)) {
                foundUser = currentUser;
            }
        }
        return foundUser;
    }

    public ArrayList<Message> getMessages(User user) {
        ArrayList<User> users = new ArrayList<User>();
        Iterator i = this.history.iterator();
        ArrayList<Message> messages = null;
        while (i.hasNext() && messages == null) {
            UserData currentUserData = (UserData)(i.next());
            if(currentUserData.getUser().equals(user)) {
                messages = currentUserData.getMessages();
            }
        }
        return messages;
    }

    public ArrayList<String> getLogins() {
        ArrayList<String> logins = new ArrayList<String>();
        for(UserData ud: this.history) {
            logins.add(ud.getUser().getLogin());
        }
        return logins;
    }
}
