package entity;

import entity.animation.Animation;
import entity.animation.AnimationBuilder;
import game.events.EventHandler;
import game.GamePanel;
import input.KeyHandler;
import utility.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents the player entity in the Punch Out game.
 * Handles player actions, animations, and state transitions.
 */
public class Player extends Entity {
    private final Animation dodgeRight, dodgeLeft, block, dodgeDown;
    private final int DODGE_SPEED = 5;
    private final Animation jabRight, jabLeft;
    private final Animation strongPunchRight, strongPunchLeft;
    private final int JAB_SPEED = 2;
    private int blockFrameCounter = 0;
    private final int BLOCK_FRAMES = 30;
    public int score = 0;
    int dodgeFrameCounter = 0;
    public int testScore = 132020;
    private final EventHandler attackEvent;
    private final KeyHandler keyH;

    /**
     * Constructs a new Player entity with the specified key handler and attack event handler.
     * Initializes animations, coordinates, and other properties.
     *
     * @param keyH        The key handler for player input.
     * @param attackEvent The event handler for player attacks.
     */
    public Player(KeyHandler keyH, EventHandler attackEvent) {
        this.keyH = keyH;
        this.attackEvent = attackEvent;
        this.worldX = GamePanel.screenWidth / 2 - GamePanel.scaledTileSize / 2;
        this.worldY = 380;

        this.entityWidth = 60;
        this.entityHeight = 160;

        BufferedImage spriteSheet;
        try {
            spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/entities/little_mac/little_mac_spritesheet.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.sprite = spriteSheet.getSubimage(1, 27, 24, 61);

        //Here's an exampel of how to add an animation with and without an array
        //These are purely for loading images in from the sprite sheet
        int SPRITE_WIDTH = 25;
        int SPRITE_HEIGHT = 62;
        BufferedImage[] a = {spriteSheet.getSubimage(50, 100, SPRITE_WIDTH, SPRITE_HEIGHT), spriteSheet.getSubimage(75, 100, SPRITE_WIDTH, SPRITE_HEIGHT)};
        BufferedImage[] blockAnim = UtilityTool.createArrayForAnimation(spriteSheet, 2, 349, 101, SPRITE_WIDTH, SPRITE_HEIGHT, this.entityWidth, this.entityHeight);
        BufferedImage[] tempDodgeDownAnim = UtilityTool.createArrayForAnimation(spriteSheet, 3, 348, 101, SPRITE_WIDTH, SPRITE_HEIGHT, this.entityWidth, this.entityHeight);
        /* TODO: REMEMBER TO TIDY THIS UP */
        BufferedImage[] dodgeDownAnim = new BufferedImage[4];
        dodgeDownAnim[0] = tempDodgeDownAnim[0];
        dodgeDownAnim[1] = tempDodgeDownAnim[1];
        dodgeDownAnim[2] = tempDodgeDownAnim[2];
        dodgeDownAnim[3] = tempDodgeDownAnim[2];

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
                .setOwnerEntity(this)
                .setAnimationWithoutArray(2)
                .setFrame(spriteSheet.getSubimage(314, 23, SPRITE_WIDTH + 4, SPRITE_HEIGHT), 0)
                .setFrame(spriteSheet.getSubimage(350, 17, SPRITE_WIDTH, SPRITE_HEIGHT + 10), 1)
                .setSpeed(12)
                .setLoop(false)
                .build();

        strongPunchRight = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(3)
                .setFrame(jabRight.getFrame(0), 0)
                .setFrame(jabRight.getFrame(1), 1)
                .setFrame(spriteSheet.getSubimage(376, 8, SPRITE_WIDTH - 1, SPRITE_HEIGHT + 19), 2)
                .setLoop(false)
                .setSpeed(9)
                .build();

        jabLeft = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(3)
                .setFrame(spriteSheet.getSubimage(130, 23, SPRITE_WIDTH + 3, SPRITE_HEIGHT), 0)
                .setFrame(spriteSheet.getSubimage(163, 23, SPRITE_WIDTH + 3, SPRITE_HEIGHT), 1)
                .setFrame(spriteSheet.getSubimage(193, 19, SPRITE_WIDTH, SPRITE_HEIGHT + 7), 2)
                .setSpeed(8)
                .setLoop(false)
                .build();

        strongPunchLeft = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(4)
                .setFrame(jabLeft.getFrame(0), 0)
                .setFrame(jabLeft.getFrame(1), 1)
                .setFrame(jabLeft.getFrame(2), 2)
                .setFrame(spriteSheet.getSubimage(220, 8, SPRITE_WIDTH, SPRITE_HEIGHT + 19), 3)
                .setLoop(false)
                .setSpeed(6)
                .build();

        block = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(blockAnim)
                .setSpeed(10)
                .setLoop(false)
                .build();

        dodgeDown = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(dodgeDownAnim)
                .setSpeed(7)
                .setLoop(false)
                .build();

        onHit = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(1)
                .setFrame(spriteSheet.getSubimage(151, 99, SPRITE_WIDTH, SPRITE_HEIGHT), 0)
                .setSpeed(0)
                .setLoop(false)
                .build();;

        this.toPlay = idle;
    }

    private void dodge(boolean isDodgeRight) {
        int m;
        if (isDodgeRight) {
            this.currentState = EntityStates.DODGE_RIGHT;
            m = 1;
        } else {
            this.currentState = EntityStates.DODGE_LEFT;
            m = -1;
        }

        if (toPlay.isAnimationDone(toPlay.getAnimationDuration()/2)) {
            this.worldX -= this.DODGE_SPEED * m;
        } else {
            this.worldX += this.DODGE_SPEED * m;
        }

        if (toPlay.isAnimationDone()) {
            this.currentState = EntityStates.IDLE;
            addCoolDown(16);
        }
    }

    private void jab(boolean isJabRight, boolean isStrongPunch) {
        int m;
        if (isStrongPunch) {
            if (isJabRight) {
                this.currentState = EntityStates.STRONG_PUNCH_RIGHT;
                m = -1;
            } else {
                this.currentState = EntityStates.STRONG_PUNCH_LEFT;
                m = 1;
            }
        } else {
            if(isJabRight) {
                this.currentState = EntityStates.JAB_RIGHT;
                m = -1;
            } else {
                this.currentState = EntityStates.JAB_LEFT;
                m = 1;
            }
        }
        if (toPlay.isAnimationDone(toPlay.getAnimationDuration()/2)) {
            this.worldY += JAB_SPEED;
            this.worldX -= (JAB_SPEED / 2) * m;
        } else {
            this.worldY -= JAB_SPEED;
            this.worldX += (JAB_SPEED / 2) * m;
        }
        if (toPlay.isAnimationDone()) {
            this.currentState = EntityStates.IDLE;
            addCoolDown(6);
        }
    }

    // block method for player that also handles the ducking animation when s is pressed twice
    private void block() {
        this.currentState = EntityStates.BLOCK;
        blockFrameCounter++;
        if (!keyH.downPressed && blockFrameCounter >= BLOCK_FRAMES) {
            blockFrameCounter = 0;
            dodgeFrameCounter = 0;
            this.currentState = EntityStates.IDLE;
            addCoolDown(6);
        } else if (keyH.doubleDownPressed) {
            blockFrameCounter = 0;
            dodgeFrameCounter = 0;
            this.currentState = EntityStates.DODGE_DOWN;
        }
    }

    private void dodgeDown() {
        this.currentState = EntityStates.DODGE_DOWN;
        if (toPlay.isAnimationDone()) {
            this.currentState = EntityStates.IDLE;
            addCoolDown(10);
        }
    }

    @Override
    protected void resetCoordinates() {
        this.worldX = GamePanel.screenWidth / 2 - GamePanel.scaledTileSize / 2;
        this.worldY = 380;
    }

    @Override
    protected void introStateUpdate() {

    }

    /**
     * Updates the player's state and position during the fight state.
     */
    @Override
    protected void fightStateUpdate() {
        if ((keyH.rightPressed && isReadyForAction()) || isDodgeRight()) {
            toPlay = dodgeRight;
            dodge(true);
        } else if ((keyH.leftPressed && isReadyForAction()) || isDodgeLeft()) {
            toPlay = dodgeLeft;
            dodge(false);
        } else if ((keyH.rightArm && keyH.upPressed && isReadyForAction()) || isStrongPunchRight()) {
            toPlay = strongPunchRight;
            //attackEvent.execute(this, 20);
            jab(true, true);
        } else if ((keyH.leftArm && keyH.upPressed && isReadyForAction()) || isStrongPunchLeft()) {
            toPlay = strongPunchLeft;
            //attackEvent.execute(this, 20);
            jab(false, true);
        } else if ((keyH.rightArm && isReadyForAction()) || isJabRight()) {
            toPlay = jabRight;
            //attackEvent.execute(this, 10);
            jab(true, false);
        } else if ((keyH.leftArm && isReadyForAction()) || isJabLeft()) {
            toPlay = jabLeft;
            //attackEvent.execute(this, 10);
            jab(false, false);
        } else if (isDodgeDown() && cooldown == 0) {
            toPlay = dodgeDown;
            dodgeDown();
        } else if ((keyH.downPressed && isReadyForAction()) || isBlock()) {
            toPlay = block;
            block();
        }

        if(isHitStun()) {
            this.worldX = GamePanel.screenWidth / 2 - GamePanel.scaledTileSize / 2;
            this.worldY = 380;
        }
    }

    /**
     * Returns a string representation of the player entity.
     *
     * @return The string representation of the player.
     */
    @Override
    public String toString() {
        return "Player";
    }
}
