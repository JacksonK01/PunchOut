package game;

import entity.Entity;
import entity.GlassJoe;
import entity.Player;
import event.EventManager;
import input.KeyHandler;
import scene.SceneRenderer;
import ui.UI;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {


    public final static int tileSize = 16;
    public final static int scale = 3;

    //One tile will be 48x48
    public final static int scaledTileSize = tileSize*scale;

    //16 Tile width, 12 tile height
    public final static int screenWidth = scaledTileSize * 16;
    public final static int screenHeight = scaledTileSize * 12;

    final int FPS = 60;

    //Corresponds with the current enemy the player will fight
    int currentLevel = 1;

    public static int timeElapsed = 0;
    public static int totalTime = 180;

    Thread gameThread;
    public SceneRenderer sceneRenderer = new SceneRenderer(this);
    public GameEngine engine = new GameEngine(this);

    UI ui = new UI(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLUE);
        this.setDoubleBuffered(true);
        this.addKeyListener(this.engine.getKeyHandler());
        this.setFocusable(true);
    }

    public GameEngine getGameEngine() {
        return engine;
    }

    public void startGameThread() {
        this.gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
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

    public void update() {
        ui.update();
        engine.update();
    }

    //The order of drawing is important
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        sceneRenderer.draw(g2);
        ui.draw(g2);
        engine.draw(g2);

        g2.dispose();
    }
}
