package entity;

import gamepanel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {

    BufferedImage spriteSheet;
    GamePanel gp;
    boolean isDodgeRight, isDodgeLeft;
    int dodgeFrameCounter = 0;
    final int DODGE_FRAMES = 20;
    int speed;


    public Player(GamePanel gp) {

        this.gp = gp;
        this.worldX = gp.screenWidth/2 - gp.scaledTileSize/2;
        this.worldY = 400;
        this.speed = 7;

        try {
            this.spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/little_mac/little_mac_spritesheet.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.sprite = spriteSheet.getSubimage(1, 27, 24, 61);
    }

    public void dodgeRight() {
        if(!isDodgeRight) {
            isDodgeRight = true;
            dodgeFrameCounter = 0;
        }

        if(dodgeFrameCounter < DODGE_FRAMES/2) {
            this.worldX += this.speed;
        } else {
            this.worldX -= this.speed;
        }

        dodgeFrameCounter++;

        if(dodgeFrameCounter >= DODGE_FRAMES) {
            isDodgeRight = false;
            dodgeFrameCounter = 0;
        }
    }

    public void dodgeLeft() {
        if(!isDodgeLeft) {
            isDodgeLeft = true;
            dodgeFrameCounter = 0;
        }

        if(dodgeFrameCounter < DODGE_FRAMES/2) {
            this.worldX -= this.speed;
        } else {
            this.worldX += this.speed;
        }

        dodgeFrameCounter++;

        if(dodgeFrameCounter >= DODGE_FRAMES) {
            isDodgeLeft = false;
            dodgeFrameCounter = 0;
        }
    }

    public void update() {
        if((gp.keyH.rightPressed || isDodgeRight) && !isDodgeLeft) {
            dodgeRight();
        }else if((gp.keyH.leftPressed || isDodgeLeft) && !isDodgeRight) {
            dodgeLeft();
        }
    }

    public void draw(Graphics2D g2) {
        //1:2 ratio for sprite (This doesn't look right imo) TODO fix the ratio
        g2.drawImage(this.sprite, this.worldX, this.worldY, gp.scaledTileSize, gp.scaledTileSize * 2, null);
    }
}
