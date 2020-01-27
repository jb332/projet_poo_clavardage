package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    JTextField loginTextZone;
    JLabel labelForTextZone;

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

    protected String getLogin() {
        return this.loginTextZone.getText();
    }

    protected void setLabelToLoginDenied() {
        this.labelForTextZone.setText("Sorry, this login is not available. Please pick another one :");
    }

    protected void setLabelToLoginChange() {
        this.labelForTextZone.setText("Pick your new login :");
    }
}
