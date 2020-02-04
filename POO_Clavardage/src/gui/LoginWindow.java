package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    /**
     * The login text zone.
     */
    JTextField loginTextZone;
    /**
     * The login label indicating the user to pick a login.
     */
    JLabel labelForTextZone;

    /**
     * Create the login window.
     * @param actionListener an action listener. GUI plays this role since all events are supposed to be handled by it.
     */
    protected LoginWindow(ActionListener actionListener) {
        super("Connection");

        this.loginTextZone = new JTextField();
        JButton okButton = new JButton("OK");
        this.labelForTextZone = new JLabel("Pick a login :");
        okButton.addActionListener(actionListener);

        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(this.loginTextZone);
        mainPane.add(okButton, BorderLayout.EAST);
        mainPane.add(this.labelForTextZone, BorderLayout.NORTH);

        this.setPreferredSize(new Dimension(300,70));
        this.getRootPane().setDefaultButton(okButton);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(mainPane, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Get my displayed login.
     * @return my displayed login
     */
    protected String getLogin() {
        return this.loginTextZone.getText();
    }

    /**
     * Set the login panel label to login denied.
     */
    protected void setLabelToLoginDenied() {
        this.labelForTextZone.setText("Sorry, this login is not available. Please pick another one :");
    }

    /**
     * Set the login panel label to login change.
     */
    protected void setLabelToLoginChange() {
        this.labelForTextZone.setText("Pick your new login :");
    }
}
