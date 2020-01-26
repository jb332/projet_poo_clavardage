package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField loginTextZone;
    private JButton okButton;

    protected LoginWindow(ActionListener actionListener) {
        super("Saisissez un pseudo :");

        this.loginTextZone = new JTextField();
        this.okButton = new JButton("OK");
        this.okButton.addActionListener(actionListener);

        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(this.loginTextZone);
        mainPane.add(this.okButton, BorderLayout.EAST);

        this.setPreferredSize(new Dimension(300,70));
        this.getRootPane().setDefaultButton(this.okButton);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(mainPane, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    protected String getLogin() {
        return this.loginTextZone.getText();
    }
}
