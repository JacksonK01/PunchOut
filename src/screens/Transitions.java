/**
 * NOT USED IN THE PROJECT
 */
package screens;
import game.GameEngine;
import game.GamePanel;
import scene.SceneRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

// make a new screen for the transitions, will be switched to when round or fight ends
public class Transitions extends JPanel {
    /** The size of each tile in pixels. */
    public final static int tileSize = 16;

    /** The scaling factor for the tiles. */
    public final static int scale = 3;

    /** The size of each scaled tile. */
    public final static int scaledTileSize = tileSize * scale;

    /** The width of the game screen. */
    public final static int screenWidth = scaledTileSize * 16;

    /** The height of the game screen. */
    public final static int screenHeight = scaledTileSize * 12;

    /** The target frames per second for the game. */
    final int FPS = 60;

    /** The thread responsible for running the game loop. */
    Thread gameThread;

    /** The renderer responsible for rendering the game scene. */
    //public SceneRenderer sceneRenderer = GamePanel.sceneRenderer;

    /** The game engine responsible for managing game logic. */
    public GameEngine engine = new GameEngine();
    private BufferedImage spriteSheet;
    private BufferedImage[] round = new BufferedImage[3];
    // 256, 224
    JFrame window;

    public Transitions(JFrame window) {
        this.setPreferredSize(new Dimension(768, 576));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.window = window;
        try {
            spriteSheet = new BufferedImage(256, 224, BufferedImage.TYPE_INT_ARGB);
            spriteSheet = ImageIO.read(getClass().getResource("/textures/transitions/transitionSprite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int x = 1; x < round.length; x++) {
            this.round[x] = spriteSheet.getSubimage(x, 12, 256, 7);
        }
    }
    public GameEngine getGameEngine() {
        return engine;
    }
    /**
     * Starts the game thread, which runs the game loop.
     */
    public void startGameThread() {
        this.gameThread = new Thread(String.valueOf(this));
        gameThread.start();
    }
    /**
     * Runs game loop.
     */
    public void run() {
        long drawInterval = 1000000000/FPS; //Draws the screen every 0.0166666 seconds aka 1/60
        long nextDrawTime = System.nanoTime() + drawInterval; //Calculates the allowed amount of time the thread has until it runs again

        while(this.gameThread != null) {
            long currentTime = System.nanoTime();
            if (System.nanoTime() - currentTime < FPS ) {
                this.update();
                this.repaint();


                try {
                    long remaningTime = nextDrawTime - System.nanoTime();
                    remaningTime = remaningTime/1000000; //convert to miliseconds

                    if (remaningTime < 0) {
                        remaningTime = 0;
                    }

                    Thread.sleep(remaningTime);

                    nextDrawTime += drawInterval;
                } catch (InterruptedException ignored) {
                    System.out.println("Thread skipped");
                }
            }
        }
    }
    /**
     * Updates the game state.
     */
    public void update() {
        engine.update();

    }
    /**
     * Paints the game scene and UI elements on the panel.
     * @param g The graphics context.
     */
    //The order of drawing is important
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        draw(g2);

        g2.dispose();
    }
    public void paintRound(Graphics2D g2) {
        g2.drawImage(round[0], 0, 0, null);
    }

    public void draw(Graphics2D g2) {
        paintRound(g2);
    }
}
