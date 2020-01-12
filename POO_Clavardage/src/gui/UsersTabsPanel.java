package gui;

import clavardage.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UsersTabsPanel extends JPanel {
    ActionListener actionListener;

    public UsersTabsPanel(ActionListener actionListener) {
        super(new GridBagLayout());
        this.actionListener = actionListener;
    }

    private UserTab getUserTab(User user) {
        Component[] usersTabs = this.getComponents();
        UserTab foundUserTab = null;

        for(int i=0; i<usersTabs.length-1 && foundUserTab == null; i++) {
            if(((UserTab)usersTabs[i]).getText() == user.getLogin()) {
                foundUserTab = (UserTab)usersTabs[i];
            }
        }
        return foundUserTab;
    }

    private void changeUserNameFontStyle(UserTab userTab, int fontStyle) {
        userTab.setFont(new Font(userTab.getFont().getName(), fontStyle, userTab.getFont().getSize()));
    }

    public void addNotificationToUserTab(User user) {
        changeUserNameFontStyle(this.getUserTab(user), Font.BOLD);
    }

    public void removeNotificationFromUserTab(User user) {
        changeUserNameFontStyle(this.getUserTab(user), 0);
    }

    public void setUserTabToOnline(User user) {
        this.getUserTab(user).setBackground(new Color(225,255,231));
    }

    public void setUserTabToOffline(User user) {
        this.getUserTab(user).setBackground(Color.LIGHT_GRAY);
    }

    public void loadUsers(ArrayList<User> users) {
        GridBagConstraints usersTabsConstraints = new GridBagConstraints();
        usersTabsConstraints.gridx = 0;
        usersTabsConstraints.gridy = GridBagConstraints.RELATIVE;
        usersTabsConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        usersTabsConstraints.weightx = 1;
        usersTabsConstraints.weighty = 0;
        usersTabsConstraints.fill = GridBagConstraints.VERTICAL;

        if(users != null) {
            for (User currentUser : users) {
                UserTab currentUserTab = new UserTab(currentUser);
                currentUserTab.addActionListener(this.actionListener);
                this.add(currentUserTab, usersTabsConstraints);
            }
        }

        usersTabsConstraints.weighty = 1;
        this.add(Box.createVerticalGlue(), usersTabsConstraints);

        this.revalidate();
        this.repaint();
    }

    public void addUser(User user) {
        GridBagConstraints userTabConstraints = new GridBagConstraints();
        userTabConstraints.gridx = 0;
        userTabConstraints.gridy = GridBagConstraints.RELATIVE;
        userTabConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        userTabConstraints.weightx = 1;
        userTabConstraints.weighty = 0;
        userTabConstraints.fill = GridBagConstraints.VERTICAL;

        UserTab userTab = new UserTab(user);
        userTab.addActionListener(this.actionListener);
        this.add(userTab, userTabConstraints, this.getComponentCount()-1);

        this.revalidate();
        this.repaint();
    }
}
