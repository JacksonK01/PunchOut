package screens;
import java.awt.*;
import javax.swing.*;

// make a new screen for the transitions, will be switched to when round or fight ends
public class Transitions extends JPanel {
    JFrame window;
    public Transitions(JFrame window) {
        this.setPreferredSize(new Dimension(768, 576));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.window = window;
    }

}
