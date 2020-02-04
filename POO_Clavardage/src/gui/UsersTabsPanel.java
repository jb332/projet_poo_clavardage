package gui;

import clavardage.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The panel containing the users you can select.
 */
public class UsersTabsPanel extends JPanel {
    ActionListener actionListener;

    /**
     * Constructor.
     * @param actionListener an action listener. GUI plays this role since all events are supposed to be handled by it.
     */
    public UsersTabsPanel(ActionListener actionListener) {
        super(new GridBagLayout());
        this.actionListener = actionListener;
    }

    /**
     * Get a user tab from a user.
     * @param user the user whose tab you want to retrieve
     * @return the found user tab or null if no user tab was found for the given user
     */
    public UserTab getUserTab(User user) {
        Component[] usersTabs = this.getComponents();
        UserTab foundUserTab = null;

        for(int i=0; i<usersTabs.length-1 && foundUserTab == null; i++) {
            if(((UserTab)usersTabs[i]).getText().equals(user.getLogin())) {
                foundUserTab = (UserTab)usersTabs[i];
            }
        }
        return foundUserTab;
    }

    /**
     * Get a user tab from a user login.
     * @param login the login of the user whose tab you want to retrieve
     * @return the found user tab or null if no user tab was found for the given user login
     */
    public UserTab getUserTab(String login) {
        Component[] usersTabs = this.getComponents();
        UserTab foundUserTab = null;

        for(int i=0; i<usersTabs.length-1 && foundUserTab == null; i++) {
            if(((UserTab)usersTabs[i]).getText().equals(login)) {
                foundUserTab = (UserTab)usersTabs[i];
            }
        }
        return foundUserTab;
    }

    /**
     * Display a list of users.
     * @param users the users you want to display
     */
    public void loadUsers(ArrayList<User> users) {
        GridBagConstraints usersTabsConstraints = new GridBagConstraints();
        usersTabsConstraints.gridx = 0;
        usersTabsConstraints.gridy = GridBagConstraints.RELATIVE;
        usersTabsConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        usersTabsConstraints.weightx = 1;
        usersTabsConstraints.weighty = 0;
        usersTabsConstraints.fill = GridBagConstraints.VERTICAL;

        if(users.size() > 0) {
            for (User currentUser : users) {
                UserTab currentUserTab = new UserTab(currentUser);
                if(currentUser.isConnected()) {
                    currentUserTab.setToOnline();
                }
                currentUserTab.addActionListener(this.actionListener);
                this.add(currentUserTab, usersTabsConstraints);
            }
        }

        usersTabsConstraints.weighty = 1;
        this.add(Box.createVerticalGlue(), usersTabsConstraints);

        this.revalidate();
        this.repaint();
    }

    /**
     * Display a user.
     * @param user the user you want to display
     */
    public void addUser(User user) {
        GridBagConstraints userTabConstraints = new GridBagConstraints();
        userTabConstraints.gridx = 0;
        userTabConstraints.gridy = GridBagConstraints.RELATIVE;
        userTabConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        userTabConstraints.weightx = 1;
        userTabConstraints.weighty = 0;
        userTabConstraints.fill = GridBagConstraints.VERTICAL;

        UserTab userTab = new UserTab(user);
        if (user.isConnected()) {
            userTab.setToOnline();
        }
        userTab.addActionListener(this.actionListener);
        this.add(userTab, userTabConstraints, 0);

        this.revalidate();
        this.repaint();
    }

    /**
     * Tell if there is no displayed user at all.
     * @return true if no user is displayed at all, false if there is at least one user displayed
     */
    public boolean isEmpty() {
        return this.getComponentCount() < 2;
    }
}
