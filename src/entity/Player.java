package entity;

import entity.animation.Animation;
import entity.animation.AnimationBuilder;
import gamepanel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    BufferedImage spriteSheet;

    //These are purely for loading images in from the sprite sheet
    final int SPRITE_WIDTH = 25;
    final int SPRITE_HEIGHT = 62;
    PlayerState currentState = PlayerState.IDLE;
    int speed;
    int cooldown;

    GamePanel gp;

    Animation dodgeRight, dodgeLeft;
    int dodgeFrameCounter = 0;
    final int DODGE_FRAMES = 20;

    Animation punchLeft, punchRight;

    public int score = 0;
    public int testScore = 132020;

    public Player(GamePanel gp) {

        this.gp = gp;
        this.worldX = gp.screenWidth/2 - gp.scaledTileSize/2;
        this.worldY = 400;
        this.speed = 5;

        this.entityWidth = gp.scaledTileSize;
        this.entityHeight = gp.scaledTileSize * 2;

        try {
            this.spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/little_mac/little_mac_spritesheet.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.sprite = spriteSheet.getSubimage(1, 27, 24, 61);

        //Here's an exampel of how to add an animation with and without an array
        BufferedImage[] a = {spriteSheet.getSubimage(50, 100, SPRITE_WIDTH, SPRITE_HEIGHT), spriteSheet.getSubimage(75, 100, SPRITE_WIDTH, SPRITE_HEIGHT)};
        dodgeLeft = AnimationBuilder.newInstance()
                .setAnimationWithArray(a)
                .setSpeed(10)
                .setLoop(false)
                .setOwnerEntity(this)
                .build();

        dodgeRight = AnimationBuilder.newInstance()
                .setAnimationWithoutArray(2)
                .setFrame(spriteSheet.getSubimage(100, 100, SPRITE_WIDTH, SPRITE_HEIGHT), 0)
                .setFrame(spriteSheet.getSubimage(125, 100, SPRITE_WIDTH, SPRITE_HEIGHT), 1)
                .setSpeed(10)
                .setLoop(false)
                .setOwnerEntity(this)
                .build();

        punchLeft = AnimationBuilder.newInstance()
                .setAnimationWithoutArray(1)
                .setFrame(spriteSheet.getSubimage(130, 23, SPRITE_WIDTH, SPRITE_HEIGHT), 0)
                .setSpeed(10)
                .setOwnerEntity(this)
                .build();
    }

    public void addCoolDown(int num) {
        this.cooldown += num;
    }

    public void setCoolDown(int num) {
        this.cooldown = num;
    }


    //dodgeFrameCounter starts at 0 before this runs
    public void dodgeRight() {
        currentState = PlayerState.DODGE_RIGHT;
        if(dodgeFrameCounter < DODGE_FRAMES/2) {
            this.worldX += this.speed;
        } else {
            this.worldX -= this.speed;
        }
        dodgeFrameCounter++;

        if(dodgeFrameCounter >= DODGE_FRAMES) {
            currentState = PlayerState.IDLE;
            dodgeFrameCounter = 0;
            addCoolDown(16);
        }
    }

    public void dodgeLeft() {
        currentState = PlayerState.DODGE_LEFT;
        if(dodgeFrameCounter < DODGE_FRAMES/2) {
            this.worldX -= this.speed;
        } else {
            this.worldX += this.speed;
        }
        dodgeFrameCounter++;

        if(dodgeFrameCounter >= DODGE_FRAMES) {
            currentState = PlayerState.IDLE;
            dodgeFrameCounter = 0;
            addCoolDown(16);
        }
    }

    public void punchLeft() {

    }

    public boolean isDodgeRight() {
        return currentState == PlayerState.DODGE_RIGHT;
    }

    public boolean isDodgeLeft() {
        return currentState == PlayerState.DODGE_LEFT;
    }

    public void update() {
        if((gp.keyH.rightPressed || isDodgeRight()) && cooldown == 0) {
            dodgeRight();
        } else if((gp.keyH.leftPressed || isDodgeLeft()) && cooldown == 0) {
            dodgeLeft();
        }
        if(cooldown > 0) {
            cooldown--;
        }
    }

    public void draw(Graphics2D g2) {
        //TODO fix using .reset() and find a way to naturally reset the animation
        if(isDodgeRight()) {
            dodgeRight.drawAnimation(g2);
        } else if(isDodgeLeft()) {
            dodgeLeft.drawAnimation(g2);
        } else {
            g2.drawImage(this.sprite, this.worldX, this.worldY, this.entityWidth, this.entityHeight, null);
            dodgeRight.reset();
            dodgeLeft.reset();
        }

        //punchLeft.drawAnimation(g2);
    }

    public enum PlayerState {
        IDLE,
        DODGE_RIGHT,
        DODGE_LEFT;
    }
}
