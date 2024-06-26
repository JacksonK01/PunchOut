package entity;

import entity.animation.Animation;
import entity.animation.AnimationBuilder;
import game.Sound;
import game.events.AttackHandler;
import game.GamePanel;
import game.events.RequestHandler;
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
    private final Animation jabRight, jabLeft;
    private final Animation strongPunchRight, strongPunchLeft;
    private final Animation walk;
    private final Animation tired;
    private int blockFrameCounter = 0;
    public int score = 0;
    int dodgeFrameCounter = 0;
    public int testScore = 132020;
    private final AttackHandler attackEvent;
    private final RequestHandler isRightHandedRequest;
    private final KeyHandler keyH;
    private int stamina = 20;
    private final Sound punch = new Sound("/sound/effect/punch.wav");

    private Animation victory;
    private Animation lose;
    private int staminaTimer;

    /**
     * Constructs a new player entity with the specified key handler, attack handler, and request handler.
     * @param keyH The key handler for the player.
     * @param attackEvent The attack handler for the player.
     * @param isRightHandedRequest The request handler for the player.
     */
    public Player(KeyHandler keyH, AttackHandler attackEvent, RequestHandler<Boolean> isRightHandedRequest) {
        this.keyH = keyH;
        this.attackEvent = attackEvent;
        this.isRightHandedRequest = isRightHandedRequest;
        this.worldX = GamePanel.screenWidth / 2 - GamePanel.scaledTileSize / 2;
        this.worldY = 500; //380
        this.punch.changeVolume(-10);
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

        this.walk = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(2)
                .setFrame(sprite, 0)
                .setFrame(spriteSheet.getSubimage(132, 23, 26, 61), 1)
                .setSpeed(10)
                .setLoop(true)
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
                .setFrame(spriteSheet.getSubimage(218, 8, SPRITE_WIDTH, SPRITE_HEIGHT + 19), 3)
                .setLoop(false)
                .setSpeed(6)
                .build();

        block = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(blockAnim)
                .setSpeed(5)
                .setLoop(false)
                .build();

        dodgeDown = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(dodgeDownAnim)
                .setSpeed(4)
                .setLoop(false)
                .build();

        onHit = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(1)
                .setFrame(spriteSheet.getSubimage(151, 99, SPRITE_WIDTH, SPRITE_HEIGHT), 0)
                .setSpeed(0)
                .setLoop(false)
                .build();

        victory = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(2)
                .setFrame(spriteSheet.getSubimage(1, 91, SPRITE_WIDTH, 71), 0)
                .setFrame(spriteSheet.getSubimage(26, 91, SPRITE_WIDTH, 71), 1)
                .setSpeed(30)
                .setLoop(true)
                .build();

        lose = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(1)
                .setFrame(spriteSheet.getSubimage(217, 91, SPRITE_WIDTH, 71), 0)
                .setSpeed(0)
                .setLoop(true)
                .build();

        tired = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(UtilityTool.createArrayForAnimation(spriteSheet, 3, 51, 27, 24, 64, this.entityWidth, this.entityHeight))
                .setSpeed(12)
                .setLoop(true)
                .build();

        this.toPlay = idle;
    }
    /**
     * Returns the player's stamina.
     * @return The player's stamina.
     */
    public int getStamina() {
        return this.stamina;
    }
    /**
     * method for the player dodge
     * @param isDodgeRight boolean for if the player is dodging right
     */
    private void dodge(boolean isDodgeRight) {
        int m;
        if (isDodgeRight) {
            this.currentState = EntityStates.DODGE_RIGHT;
            m = 1;
        } else {
            this.currentState = EntityStates.DODGE_LEFT;
            m = -1;
        }
        int DODGE_SPEED = 5;
        if (toPlay.isAnimationDone(toPlay.getAnimationDuration()/2)) {
            this.worldX -= DODGE_SPEED * m;
        } else {
            this.worldX += DODGE_SPEED * m;
        }

        if (toPlay.isAnimationDone()) {
            this.currentState = EntityStates.IDLE;
            addCoolDown(16);
        }
    }
    /**
     * method for the player jab
     * @param isJabRight boolean for if the player is jabbing right
     * @param isStrongPunch boolean for if the player is doing a strong punch
     */
    private void jab(boolean isJabRight, boolean isStrongPunch) {
        if (toPlay.getDuration() <= 1) {
            punch.play();
        }
        int m;
        int JAB_SPEED;
        int damage;
        if (isStrongPunch) {
            JAB_SPEED = 5;
            damage = 3;
            if (isJabRight) {
                this.currentState = EntityStates.STRONG_PUNCH_RIGHT;
                m = -1;
            } else {
                this.currentState = EntityStates.STRONG_PUNCH_LEFT;
                m = 1;
            }
        } else {
            JAB_SPEED = 2;
            damage = 1;
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
            this.worldX -= m;
            this.attackEvent.execute(this, damage);
        } else {
            this.worldY -= JAB_SPEED;
            this.worldX += m;
        }
        if (toPlay.isAnimationDone()) {
            this.currentState = EntityStates.IDLE;
            addCoolDown(10);
            if (isStrongPunch) {
                stamina -= 4;
            } else {
                stamina--;
            }
        }
    }

    /**
     * block method for player that also handles the ducking animation when s is pressed twice
     */
    private void block() {
        this.currentState = EntityStates.BLOCK;
        blockFrameCounter++;
        staminaTimer--;
        int BLOCK_FRAMES = 30;
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
    /**
     * method for the player to dodge down
     */
    private void dodgeDown() {
        this.currentState = EntityStates.DODGE_DOWN;
        if (toPlay.isAnimationDone()) {
            this.currentState = EntityStates.IDLE;
            addCoolDown(14);
        }
    }
    /**
     * overides the resetCoordinates method in Entity
     */
    @Override
    protected void resetCoordinates() {
        this.worldX = GamePanel.screenWidth / 2 - GamePanel.scaledTileSize / 2;
        this.worldY = 380;
    }
    /**
     * overides the intro state update method in Entity
     */
    @Override
    protected void introStateUpdate() {
        toPlay = walk;
        if(worldY > 380) {
            worldY--;
        } else {
            toPlay = idle;
        }
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
            jab(true, true);
        } else if ((keyH.leftArm && keyH.upPressed && isReadyForAction()) || isStrongPunchLeft()) {
            toPlay = strongPunchLeft;
            jab(false, true);
        } else if ((keyH.rightArm && isReadyForAction()) || isJabRight()) {
            toPlay = jabRight;
            jab(true, false);
        } else if ((keyH.leftArm && isReadyForAction()) || isJabLeft()) {
            toPlay = jabLeft;
            jab(false, false);
        } else if (isDodgeDown() && cooldown == 0) {
            toPlay = dodgeDown;
            dodgeDown();
        } else if ((keyH.downPressed && isReadyForAction()) || isBlock()) {
            toPlay = block;
            block();
        }

        if (this.stamina < 0) {
            this.stamina = 0;
        }
        if (this.stamina == 0 || isOutOfStamina()) {
            this.currentState = EntityStates.OUT_OF_STAMINA;
            this.isOutOfStaminaMode = true;
            toPlay = tired;
            if(staminaTimer > 80) {
                stamina++;
                staminaTimer = 0;
            }
            if(this.stamina >= 10) {
                this.currentState = EntityStates.IDLE;
                this.isOutOfStaminaMode = false;
                toPlay = idle;
            }
        }
        if (staminaTimer > 100 && stamina < 20) {
            this.stamina++;
            staminaTimer = 0;
        }
        if(stamina < 20) {
            staminaTimer++;
        }
    }
    /**
     * overides the end state update method in Entity
     */
    @Override
    protected void endStateUpdate() {
        if(getHealth() > 0) {
            this.toPlay = victory;
        } else {
            this.toPlay = lose;
        }
    }
    /**
     * overides the onHit method in Entity
     */
    @Override
    protected void onHit() {
        super.onHit();

    }

    /**
     * Returns a string representation of the player entity.
     * @return The string representation of the player.
     */
    @Override
    public String toString() {
        return "Player";
    }
}
