package clavardage;

import javax.swing.*;

public class UserButton extends JButton {
    private User user;

    public UserButton(User user) {
        super(user.getLogin());
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
