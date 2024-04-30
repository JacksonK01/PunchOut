package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//This is only for keyboard inputs
public class KeyHandler implements KeyListener {
    //Movement
    public boolean upPressed, rightPressed, downPressed, leftPressed, doubleDownPressed, escape;
    private boolean downReleased = true;

    //Attack
    public boolean rightArm, leftArm;
    //Time of last key press for s
    private long lastSPressTime = 200;
    //Threshold for double press in milliseconds
    private static final long DOUBLE_PRESS_THRESHOLD = 200;
    /**
     * Invoked when a key has been typed.
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }
    /**
     * Invoked when a key has been pressed.
     * @param e The KeyEvent object.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSPressTime <= DOUBLE_PRESS_THRESHOLD && currentTime - lastSPressTime > 20 && downReleased) {
                doubleDownPressed = true;
            } else {
                downPressed = true;
            }
            lastSPressTime = currentTime;
            downReleased = false; // Set downReleased to false when 'S' is pressed
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_L) {
            rightArm = true;
        }
        if (code == KeyEvent.VK_K) {
            leftArm = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            escape = true;
        }
    }

    /**
     * Invoked when a key has been released.
     * @param e The KeyEvent object.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
            doubleDownPressed = false;
            downReleased = true; // Set downReleased to true when 'S' is released
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_L) {
            rightArm = false;
        }
        if (code == KeyEvent.VK_K) {
            leftArm = false;
        }
    }
}