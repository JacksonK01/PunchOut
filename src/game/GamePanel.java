package game;

import scene.SceneRenderer;
import ui.UI;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the main game panel for the Punch Out game.
 * This panel handles rendering the game scene, UI elements, and game engine logic.
 */
public class GamePanel extends JPanel implements Runnable {

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

    /** The current level of the game. */
    int currentLevel = 1;

    /** The total time elapsed in the game. */
    public static int timeElapsed = 0;

    /** The total time allowed for the game. */
    public static int totalTime = 180;

    /** The thread responsible for running the game loop. */
    Thread gameThread;

    /** The renderer responsible for rendering the game scene. */
    public SceneRenderer sceneRenderer = new SceneRenderer(this);

    /** The game engine responsible for managing game logic. */
    public GameEngine engine = new GameEngine();

    /** The user interface for displaying game information. */
    UI ui = new UI(this);

    Sound themeSong = new Sound("/sound/music/fight_theme.wav");
    Sound crowd = new Sound("/sound/effect/crowd_noise.wav");

    /**
     * Constructs a new GamePanel with default dimensions and settings.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLUE);
        this.setDoubleBuffered(true);
        this.addKeyListener(this.engine.getKeyHandler());
        this.setFocusable(true);

        themeSong.changeVolume(-15);
        themeSong.play();
        themeSong.loop();

        crowd.changeVolume(-27);
        crowd.play();
        crowd.loop();
    }
    /**
     * Retrieves the game engine associated with this game panel.
     * @return The game engine.
     */
    public GameEngine getGameEngine() {
        return engine;
    }
    /**
     * Starts the game thread, which runs the game loop.
     */
    public void startGameThread() {
        this.gameThread = new Thread(this);
        gameThread.start();
    }
    /**
     * Runs game loop.
     */
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
    /**
     * Updates the game state.
     */
    public void update() {
        engine.update();

        if (GamePhaseManager.getGlobalEventState() != GamePhase.INTRO) {
            crowd.stop();
        }
        if (GamePhaseManager.getGlobalEventState() == GamePhase.END) {
            themeSong.stop();
        }

        if (engine.getKeyHandler().escape) {
            System.exit(0);
        }

    }
    /**
     * Paints the game scene and UI elements on the panel.
     * @param g The graphics context.
     */
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
