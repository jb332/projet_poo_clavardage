package gui;

import clavardage.User;

import javax.swing.*;
import java.awt.*;

public class UserTab extends JButton {
    public UserTab(User user) {
        super(user.getLogin());
        super.setVerticalAlignment(JButton.TOP);
        super.setFont(new Font(super.getFont().getName(), 0, super.getFont().getSize()));
    }
}
