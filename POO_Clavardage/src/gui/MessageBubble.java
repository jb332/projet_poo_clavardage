package gui;

import clavardage.Message;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class MessageBubble extends JPanel {
    public MessageBubble(Message message) {
        super(new BorderLayout());

        JLabel content = new JLabel(message.getContent());
        content.setFont(new Font(content.getFont().getName(), content.getFont().getStyle(), content.getFont().getSize()*3/2));
        JLabel date = new JLabel(message.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy")));
        date.setFont(new Font(date.getFont().getName(), 0, date.getFont().getSize()*3/4));

        JPanel auxPane = new JPanel(new BorderLayout());
        auxPane.add(content);
        auxPane.add(date, BorderLayout.SOUTH);
        auxPane.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, this.getBackground()));

        if(message.isSent()) {
            auxPane.setBackground(Color.BLUE);
        } else {
            auxPane.setBackground(Color.PINK);
        }

        this.add(auxPane, BorderLayout.NORTH);
    }
}
