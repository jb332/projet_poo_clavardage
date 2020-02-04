package gui;

import clavardage.User;

import javax.swing.*;
import java.awt.*;

/**
 * A button representing a user you can select.
 */
public class UserTab extends JButton {
    /**
     * Constructor.
     * @param user the corresponding user
     */
    public UserTab(User user) {
        super(user.getLogin());
        super.setVerticalAlignment(JButton.TOP);
        super.setFont(new Font(super.getFont().getName(), super.getFont().getStyle(), 2*super.getFont().getSize()));
        this.setToOffline();
    }

    /**
     * Change the font style of the user name.
     * @param fontStyle a code representing the font style. 0 for normal and Font.BOLD for bold.
     */
    private void changeUserNameFontStyle(int fontStyle) {
        super.setFont(new Font(super.getFont().getName(), fontStyle, super.getFont().getSize()));
    }

    /**
     * Add a notification for incoming message on the user tab. The user name becomes bold.
     */
    public void addNotification() {
        this.changeUserNameFontStyle(Font.BOLD);
    }

    /**
     * Remove the notification for incoming message from the user tab. The user name becomes normal.
     */
    public void removeNotification() {
        this.changeUserNameFontStyle(0);
    }

    /**
     * Change the user status on the user tab to online by making the button background beige.
     */
    public void setToOnline() {
        super.setBackground(new Color(225,255,231));
    }

    /**
     * Change the user status on the user tab to offline by making the button background grey.
     */
    public void setToOffline() {
        super.setBackground(Color.LIGHT_GRAY);
    }

    /**
     * Select the user tab by adding a border around it.
     */
    public void select() {
        super.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    /**
     * Unselect the user tab by removing the border around it.
     */
    public void unSelect() {
        super.setBorder(BorderFactory.createEmptyBorder());
    }
}
