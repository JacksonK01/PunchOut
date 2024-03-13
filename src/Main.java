import gamepanel.GamePanel;

import javax.swing.*;

//All assets are from https://www.spriters-resource.com/nes/punchout/
public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Punch Out");

        GamePanel gamePanel = new GamePanel();
        window.setContentPane(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}