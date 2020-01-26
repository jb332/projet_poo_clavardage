package gui;

import clavardage.User;

import javax.swing.*;
import java.awt.*;

public class UserTab extends JButton {
    public UserTab(User user) {
        super(user.getLogin());
        super.setVerticalAlignment(JButton.TOP);
        super.setFont(new Font(super.getFont().getName(), super.getFont().getStyle(), 2*super.getFont().getSize()));
        this.setToOffline();
    }

    private void changeUserNameFontStyle(int fontStyle) {
        super.setFont(new Font(super.getFont().getName(), fontStyle, super.getFont().getSize()));
    }

    public void addNotification() {
        this.changeUserNameFontStyle(Font.BOLD);
    }

    public void removeNotification() {
        this.changeUserNameFontStyle(0);
    }

    public void setToOnline() {
        super.setBackground(new Color(225,255,231));
    }

    public void setToOffline() {
        super.setBackground(Color.LIGHT_GRAY);
    }

    public void select() {
        super.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void unSelect() {
        super.setBorder(BorderFactory.createEmptyBorder());
    }
}
