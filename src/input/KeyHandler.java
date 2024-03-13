package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//This is only for keyboard inputs
public class KeyHandler implements KeyListener {
    //Movement
    public boolean upPressed, rightPressed, downPressed, leftPressed;

    //Attack
    public boolean rightArm, leftArm;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_KP_UP) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_KP_RIGHT) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_KP_DOWN) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_KP_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_X) {
            rightArm = true;
        }
        if (code == KeyEvent.VK_Z) {
            leftArm = true;
        }
    }

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
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_X) {
            rightArm = false;
        }
        if (code == KeyEvent.VK_Z) {
            leftArm = false;
        }
    }

    public boolean isAnyMoveKeyPressed() {
        return upPressed || downPressed || leftPressed || rightPressed;
    }
}