package entity;

import entity.animation.Animation;
import entity.animation.AnimationBuilder;
import gamepanel.GamePanel;
import utility.UtilityTool;

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
    int cooldown;

    GamePanel gp;

    Animation dodgeRight, dodgeLeft;
    int dodgeFrameCounter = 0;
    final int DODGE_FRAMES = 20;
    final int DODGE_SPEED = 5;

    Animation jabRight, jabLeft;
    int jabFrameCounter = 0;
    final int JAB_FRAMES = 20;
    final int JAB_SPEED = 2;

    public int score = 0;
    public int testScore = 132020;

    public Player(GamePanel gp) {

        this.gp = gp;
        this.worldX = gp.screenWidth/2 - gp.scaledTileSize/2;
        this.worldY = 400;

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
        UtilityTool.scaleImage(a, entityWidth, entityHeight);
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

        jabRight = AnimationBuilder.newInstance()
                .setAnimationWithoutArray(2)
                .setFrameAndSize(spriteSheet.getSubimage(314, 23, SPRITE_WIDTH + 4, SPRITE_HEIGHT), entityWidth + 4 * gp.scale, entityHeight, 0)
                .setFrameAndSize(spriteSheet.getSubimage(350, 17, SPRITE_WIDTH, SPRITE_HEIGHT + 10), entityWidth, entityHeight + 7 * gp.scale, 1)
                .setSpeed(10)
                .setLoop(false)
                .setOwnerEntity(this)
                .build();

        jabLeft = AnimationBuilder.newInstance()
                .setAnimationWithoutArray(3)
                .setFrameAndSize(spriteSheet.getSubimage(130, 23, SPRITE_WIDTH + 3, SPRITE_HEIGHT), entityWidth + 3 * gp.scale, entityHeight, 0)
                .setFrameAndSize(spriteSheet.getSubimage(163, 23, SPRITE_WIDTH + 3, SPRITE_HEIGHT), entityWidth + 3 * gp.scale, entityHeight, 1)
                .setFrameAndSize(spriteSheet.getSubimage(193, 19, SPRITE_WIDTH, SPRITE_HEIGHT + 7), entityWidth, entityHeight + (3 * gp.scale), 2)
                .setSpeed(6)
                .setLoop(false)
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
            this.worldX += this.DODGE_SPEED;
        } else {
            this.worldX -= this.DODGE_SPEED;
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
            this.worldX -= this.DODGE_SPEED;
        } else {
            this.worldX += this.DODGE_SPEED;
        }
        dodgeFrameCounter++;

        if(dodgeFrameCounter >= DODGE_FRAMES) {
            currentState = PlayerState.IDLE;
            dodgeFrameCounter = 0;
            addCoolDown(16);
        }
    }

    public void jabRight() {
        currentState = PlayerState.JAB_RIGHT;
        if(jabFrameCounter < JAB_FRAMES/2) {
            this.worldY -= JAB_SPEED;
            this.worldX -= JAB_SPEED/2;
        } else {
            this.worldY += JAB_SPEED;
            this.worldX += JAB_SPEED/2;
        }
        jabFrameCounter++;
        if(jabFrameCounter >= JAB_FRAMES) {
            jabFrameCounter = 0;
            currentState = PlayerState.IDLE;
            addCoolDown(6);
        }
    }

    public void jabLeft() {
        currentState = PlayerState.JAB_LEFT;
        if(jabFrameCounter < JAB_FRAMES/2) {
            this.worldY -= JAB_SPEED;
            this.worldX += JAB_SPEED/2;
        } else {
            this.worldY += JAB_SPEED;
            this.worldX -= JAB_SPEED/2;
        }
        jabFrameCounter++;
        if(jabFrameCounter >= JAB_FRAMES) {
            jabFrameCounter = 0;
            currentState = PlayerState.IDLE;
            addCoolDown(6);
        }
    }

    public boolean isDodgeRight() {
        return currentState == PlayerState.DODGE_RIGHT;
    }

    public boolean isDodgeLeft() {
        return currentState == PlayerState.DODGE_LEFT;
    }

    public boolean isJabRight() {return currentState == PlayerState.JAB_RIGHT;}

    public boolean isJabLeft() {return currentState == PlayerState.JAB_LEFT;}

    public boolean isReadyForAction() {return currentState == PlayerState.IDLE && cooldown == 0;}

    public void update() {
        if((gp.keyH.rightPressed && isReadyForAction()) || isDodgeRight()) {
            dodgeRight();
        } else if((gp.keyH.leftPressed && isReadyForAction()) || isDodgeLeft()) {
            dodgeLeft();
        } else if((gp.keyH.rightArm && isReadyForAction()) || isJabRight()) {
            jabRight();
        } else if((gp.keyH.leftArm && isReadyForAction()) || isJabLeft()) {
            jabLeft();
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
        } else if(isJabRight()) {
            jabRight.drawAnimation(g2);
        } else if(isJabLeft()) {
            jabLeft.drawAnimation(g2);
        }
        else {
            g2.drawImage(this.sprite, this.worldX, this.worldY, this.entityWidth, this.entityHeight, null);
            animationRegistryReset();
        }
    }

    public enum PlayerState {
        IDLE,
        DODGE_RIGHT,
        DODGE_LEFT,
        JAB_RIGHT,
        JAB_LEFT;
    }
}
