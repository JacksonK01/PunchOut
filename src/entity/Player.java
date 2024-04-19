package entity;

import entity.animation.Animation;
import entity.animation.AnimationBuilder;
import event.EventHandler;
import game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {

    private PlayerState playerState = PlayerState.IDLE;
    private int cooldown;

    final private GamePanel gp;

    private final Animation dodgeRight, dodgeLeft;
    private int dodgeFrameCounter = 0;
    private final int DODGE_FRAMES = 20;
    private final int DODGE_SPEED = 5;

    private final Animation jabRight, jabLeft;
    private int jabFrameCounter = 0;
    private final int JAB_FRAMES = 20;
    private final int JAB_SPEED = 2;

    private Animation duck;

    public int score = 0;
    public int testScore = 132020;

    private final EventHandler attackEvent;

    public Player(GamePanel gp, EventHandler attackEvent) {

        this.gp = gp;
        this.attackEvent = attackEvent;
        this.worldX = GamePanel.screenWidth /2 - GamePanel.scaledTileSize /2;
        this.worldY = 400;

        this.entityWidth = 60;
        this.entityHeight = 110;

        BufferedImage spriteSheet;
        try {
            spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/little_mac/little_mac_spritesheet.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.sprite = spriteSheet.getSubimage(1, 27, 24, 61);

        //Here's an exampel of how to add an animation with and without an array
        //These are purely for loading images in from the sprite sheet
        int SPRITE_WIDTH = 25;
        int SPRITE_HEIGHT = 62;
        BufferedImage[] a = {spriteSheet.getSubimage(50, 100, SPRITE_WIDTH, SPRITE_HEIGHT), spriteSheet.getSubimage(75, 100, SPRITE_WIDTH, SPRITE_HEIGHT)};

        this.idle = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(1)
                .setFrame(sprite, 0)
                .setSpeed(0)
                .setLoop(false)
                .build();

        dodgeLeft = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(a)
                .setSpeed(10)
                .setLoop(false)
                .build();

        dodgeRight = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(2)
                .setFrame(spriteSheet.getSubimage(100, 100, SPRITE_WIDTH, SPRITE_HEIGHT), 0)
                .setFrame(spriteSheet.getSubimage(125, 100, SPRITE_WIDTH, SPRITE_HEIGHT), 1)
                .setSpeed(10)
                .setLoop(false)
                .build();

        jabRight = AnimationBuilder.newInstance()
                .setAnimationWithoutArray(2)
                .setFrameAndSize(spriteSheet.getSubimage(314, 23, SPRITE_WIDTH + 4, SPRITE_HEIGHT), entityWidth + 4 * gp.scale, entityHeight, 0)
                .setFrameAndSize(spriteSheet.getSubimage(350, 17, SPRITE_WIDTH, SPRITE_HEIGHT + 10), entityWidth, entityHeight + 7 * gp.scale, 1)
                .setSpeed(12)
                .setLoop(false)
                .setOwnerEntity(this)
                .build();

        jabLeft = AnimationBuilder.newInstance()
                .setAnimationWithoutArray(3)
                .setFrameAndSize(spriteSheet.getSubimage(130, 23, SPRITE_WIDTH + 3, SPRITE_HEIGHT), entityWidth + 3 * gp.scale, entityHeight, 0)
                .setFrameAndSize(spriteSheet.getSubimage(163, 23, SPRITE_WIDTH + 3, SPRITE_HEIGHT), entityWidth + 3 * gp.scale, entityHeight, 1)
                .setFrameAndSize(spriteSheet.getSubimage(193, 19, SPRITE_WIDTH, SPRITE_HEIGHT + 7), entityWidth, entityHeight + (3 * gp.scale), 2)
                .setSpeed(8)
                .setLoop(false)
                .setOwnerEntity(this)
                .build();

        this.toPlay = idle;
    }

    private void addCoolDown(int num) {
        this.cooldown += num;
    }

    private void setCoolDown(int num) {
        this.cooldown = num;
    }


    //dodgeFrameCounter starts at 0 before this runs
    private void dodgeRight() {
        playerState = PlayerState.DODGE_RIGHT;
        if(dodgeFrameCounter < DODGE_FRAMES/2) {
            this.worldX += this.DODGE_SPEED;
        } else {
            this.worldX -= this.DODGE_SPEED;
        }
        dodgeFrameCounter++;

        if(dodgeFrameCounter >= DODGE_FRAMES) {
            playerState = PlayerState.IDLE;
            dodgeFrameCounter = 0;
            addCoolDown(16);
        }
    }

    private void dodgeLeft() {
        playerState = PlayerState.DODGE_LEFT;
        if(dodgeFrameCounter < DODGE_FRAMES/2) {
            this.worldX -= this.DODGE_SPEED;
        } else {
            this.worldX += this.DODGE_SPEED;
        }
        dodgeFrameCounter++;

        if(dodgeFrameCounter >= DODGE_FRAMES) {
            playerState = PlayerState.IDLE;
            dodgeFrameCounter = 0;
            addCoolDown(16);
        }
    }

    private void jabRight() {
        playerState = PlayerState.JAB_RIGHT;
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
            playerState = PlayerState.IDLE;
            addCoolDown(6);
        }
    }

    private void jabLeft() {
        playerState = PlayerState.JAB_LEFT;
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
            playerState = PlayerState.IDLE;
            addCoolDown(6);
        }
    }

    private boolean isDodgeRight() {
        return playerState == PlayerState.DODGE_RIGHT;
    }

    private boolean isDodgeLeft() {
        return playerState == PlayerState.DODGE_LEFT;
    }

    private boolean isJabRight() {
        return playerState == PlayerState.JAB_RIGHT;
    }

    private boolean isJabLeft() {
        return playerState == PlayerState.JAB_LEFT;
    }

    private boolean isReadyForAction() {
        return playerState == PlayerState.IDLE && cooldown == 0;
    }

    @Override
    protected void introStateUpdate() {

    }
//Event listener, class to stor
    /*

     */
    @Override
    protected void fightStateUpdate() {
        setCurrentStateIdle();
        if((gp.keyH.rightPressed && isReadyForAction()) || isDodgeRight()) {
            toPlay = dodgeRight;
            dodgeRight();
            setCurrentStateDodging();
        } else if((gp.keyH.leftPressed && isReadyForAction()) || isDodgeLeft()) {
            toPlay = dodgeLeft;
            dodgeLeft();
            setCurrentStateDodging();
        } else if((gp.keyH.rightArm && isReadyForAction()) || isJabRight()) {
            toPlay = jabRight;
            attackEvent.execute(this, 10);
            jabRight();
            setCurrentStateAttacking();
        } else if((gp.keyH.leftArm && isReadyForAction()) || isJabLeft()) {
            toPlay = jabLeft;
            attackEvent.execute(this, 10);
            jabLeft();
            setCurrentStateAttacking();
        }

        if(cooldown > 0) {
            cooldown--;
        }
    }

    @Override
    public String toString() {
        return "Player";
    }

    private enum PlayerState {
        IDLE,
        DODGE_RIGHT,
        DODGE_LEFT,
        JAB_RIGHT,
        JAB_LEFT,
        OUT_OF_STAMINA;
    }
}
