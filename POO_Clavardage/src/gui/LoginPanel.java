package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The panel containing the user login and the change login button.
 */
public class LoginPanel extends JPanel {
    private JLabel loginText;
    /**
     * Constructor.
     * @param actionListener an action listener. GUI plays this role since all events are supposed to be handled by it.
     */
    public LoginPanel(ActionListener actionListener) {
        super(new BorderLayout());

        this.loginText = new JLabel();
        JButton changeLoginButton = new JButton("Edit");
        changeLoginButton.addActionListener(actionListener);

        this.add(this.loginText);
        this.add(changeLoginButton, BorderLayout.EAST);

        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 1, 0);
        Border greyBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY);
        Border compoundBorderAux = new CompoundBorder(emptyBorder, greyBorder);
        Border compoundBorder = new CompoundBorder(compoundBorderAux, emptyBorder);
        this.setBorder(compoundBorder);
    }

    /**
     * Change my displayed login.
     * @param myLogin my new login to display
     */
    public void changeMyLogin(String myLogin) {
        this.loginText.setText(myLogin);
    }
}
