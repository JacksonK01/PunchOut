import gamepanel.GamePanel;
import screens.Transitions;

import javax.swing.*;

//All assets are from https://www.spriters-resource.com/nes/punchout/
public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Punch Out");

        Transitions transitions = new Transitions(window);
        GamePanel gamePanel = new GamePanel();
        window.setContentPane(transitions);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}