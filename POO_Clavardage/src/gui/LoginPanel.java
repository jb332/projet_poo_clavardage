package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    public LoginPanel(ActionListener actionListener) {
        super(new BorderLayout());

        JLabel loginText = new JLabel();
        JButton changeLoginButton = new JButton("Edit");
        changeLoginButton.addActionListener(actionListener);

        this.add(loginText);
        this.add(changeLoginButton, BorderLayout.EAST);

        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 1, 0);
        Border greyBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY);
        Border compoundBorderAux = new CompoundBorder(emptyBorder, greyBorder);
        Border compoundBorder = new CompoundBorder(compoundBorderAux, emptyBorder);
        this.setBorder(compoundBorder);
    }

    public void changeMyLogin(String myLogin) {
        ((JLabel)this.getComponent(0)).setText(myLogin);
    }
}
