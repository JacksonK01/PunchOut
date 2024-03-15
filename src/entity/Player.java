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

    GamePanel gp;

    Animation dodgeRight, dodgeLeft;

    boolean isDodgeRight, isDodgeLeft;
    int dodgeFrameCounter = 0;
    final int DODGE_FRAMES = 20;

    int speed;
    int cooldown;


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
    }

    public void addCoolDown(int num) {
        this.cooldown += num;
    }

    public void setCoolDown(int num) {
        this.cooldown = num;
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
            addCoolDown(16);
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
            addCoolDown(16);
        }
    }

    public void update() {
        if((gp.keyH.rightPressed || isDodgeRight) && !isDodgeLeft && cooldown == 0) {
            dodgeRight();
        }else if((gp.keyH.leftPressed || isDodgeLeft) && !isDodgeRight && cooldown == 0) {
            dodgeLeft();
        }

        if(cooldown > 0) {
            cooldown--;
        }
    }

    public void draw(Graphics2D g2) {
        //1:2 ratio for sprite (This doesn't look right imo) TODO fix the ratio
        //TODO fix using .reset() and find a way to naturally reset the animation
        if(isDodgeRight) {
            dodgeRight.setPosAndDrawAnimation(this.worldX, this.worldY, g2);
        } else if(isDodgeLeft) {
            dodgeLeft.setPosAndDrawAnimation(this.worldX, this.worldY, g2);
        } else {
            g2.drawImage(this.sprite, this.worldX, this.worldY, this.entityWidth, this.entityHeight, null);
            dodgeRight.reset();
            dodgeLeft.reset();
        }
//        if(isDodgeRight) {
//            dodgeRight.setPosAndDrawAnimation(this.worldX, this.worldY, g2);
//        } else {
//            dodgeLeft.setPosAndDrawAnimation(this.worldX, this.worldY, g2);
//        }
    }
}
