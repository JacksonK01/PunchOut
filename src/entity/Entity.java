package entity;

import entity.animation.Animation;
import game.GamePhaseManager;
import game.GamePhase;
import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The mother class for all entities in the Punch Out game.
 * Provides common functionality and properties for entities.
 */
public abstract class Entity {
    protected int health = 50;
    protected int maxHealth = 50;
    protected int worldX, worldY;
    protected BufferedImage sprite;
    /**
     * A list to store animations associated with this entity.
     * Each animation represents a specific action or state of the entity.
     * Animation will automatically be added to the registry if an owner entity is assigned to the animation.
     */
    protected ArrayList<Animation> animationRegistry = new ArrayList<>();
    protected EntityStates currentState = EntityStates.IDLE;
    //This is used for objects like Animation
    protected int entityWidth;
    protected int entityHeight;
    protected final int PLAYER_X_REST_POINT = GamePanel.screenWidth /2 - GamePanel.scaledTileSize /2;
    protected final int PLAYER_Y_REST_POINT = 400;
    protected Animation idle;
    protected Animation toPlay;
    private int hitStunFrames = 0;
    protected int cooldown = 0;
    protected Animation onHit;
    protected Animation onHitStrongLeft, onHitStrongRight;
    protected boolean isOutOfStaminaMode;

    /**
     * Placeholder
     */
    public Entity() {}
    /**
     * returns entity x position
     */
    public int getWorldX() {
        return this.worldX;
    }
    /**
     * returns entity y position
     */
    public int getWorldY() {
        return this.worldY;
    }
    /**
     * returns entity sprite width
     */
    public int getEntityWidth() {
        return entityWidth;
    }
    /**
     * returns entity sprite height
     */
    public int getEntityHeight() {
        return entityHeight;
    }
    /**
     * returns entity health
     */
    public int getHealth() {
        return this.health;
    }
    /**
     * returns entity max health
     */
    public int getMaxHealth() {
        return this.maxHealth;
    }
    /**
     * returns if entity is dodging right
     */
    protected boolean isDodgeRight() {
        return this.currentState == EntityStates.DODGE_RIGHT;
    }

    protected boolean isDodgeLeft() {
        return this.currentState == EntityStates.DODGE_LEFT;
    }

    protected boolean isJabRight() {
        return this.currentState == EntityStates.JAB_RIGHT;
    }

    protected boolean isJabLeft() {
        return this.currentState == EntityStates.JAB_LEFT;
    }
    public boolean isStrongPunch() {
        return this.currentState == EntityStates.STRONG_PUNCH_RIGHT || this.currentState == EntityStates.STRONG_PUNCH_LEFT;
    }
    protected boolean isStrongPunchRight() {
        return this.currentState == EntityStates.STRONG_PUNCH_RIGHT;
    }

    protected boolean isStrongPunchLeft() {
        return this.currentState == EntityStates.STRONG_PUNCH_LEFT;
    }

    protected boolean isBlock() {
        return this.currentState == EntityStates.BLOCK;
    }

    protected boolean isDodgeDown() {
        return this.currentState == EntityStates.DODGE_DOWN;
    }

    protected boolean isOutOfStamina() {
        return this.currentState == EntityStates.OUT_OF_STAMINA;
    }

    protected boolean isKnockedOut() {
        return this.currentState == EntityStates.KNOCKED_OUT;
    }

    public boolean isIdle() {
        return this.currentState == EntityStates.IDLE;
    }

    public boolean isHitStun() {
        return this.currentState == EntityStates.HIT_STUN;
    }

    //If this is false, then you can assume it's left
    public boolean isRightHand() {
        return isStrongPunchRight() || isJabRight();
    }

    public boolean isAttacking() {
        return this.currentState == EntityStates.JAB_RIGHT ||
                this.currentState == EntityStates.JAB_LEFT ||
                this.currentState == EntityStates.STRONG_PUNCH_RIGHT ||
                this.currentState == EntityStates.STRONG_PUNCH_LEFT;
    }

    public boolean isDodging() {
        return this.currentState == EntityStates.DODGE_DOWN ||
                this.currentState == EntityStates.BLOCK ||
                this.currentState == EntityStates.DODGE_LEFT ||
                this.currentState == EntityStates.DODGE_RIGHT;
    }
    protected boolean isReadyForAction() {
        return cooldown == 0 && isIdle();
    }

    /**
     * Adds the given animation to the animation registry of the entity.
     * @param a The animation to add to the registry.
     */
    public void addAnimationToRegistry(Animation a) {
        animationRegistry.add(a);
    }
    /**
     * Resets all animations in the registry except for the specified animation.
     * @param dontResetThisAnimation The animation to exclude from the reset operation.
     */
    private void animationRegistryReset(Animation dontResetThisAnimation) {
        animationRegistry.forEach(animation -> {
                    if (animation != dontResetThisAnimation) {
                        animation.reset();
                    }
                }
        );
    }

    public void setCurrentEntityState(EntityStates state) {
        this.currentState = state;
    }

    protected void addCoolDown(int num) {
        this.cooldown += num;
    }
    public void setStateToHit() {
        this.currentState = EntityStates.HIT_STUN;
    }
    public void doDamage(int damage) {
        this.health -= damage;
        if (health <= 0) {
            this.currentState = EntityStates.KNOCKED_OUT;
        }
    }
    /**
     * Updates the entity's state and animations based on the current game state.
     */
    public void update() {
        toPlay = idle;
        if (GamePhaseManager.getGlobalEventState() == GamePhase.INTRO) {
            introStateUpdate();
        } else if (GamePhaseManager.getGlobalEventState() == GamePhase.FIGHT) {
            if(isHitStun()) {
                onHit();
                resetCoordinates();
            } else {
                fightStateUpdate();
            }
            if (cooldown > 0) {
                cooldown--;
            }
            if (isIdle()) {
                resetCoordinates();
            }
        } else if (GamePhaseManager.getGlobalEventState() == GamePhase.END) {
            endStateUpdate();
            resetCoordinates();
        }
    }
    /**
     * Draws the entity on the provided graphics context.
     * @param g2 The graphics context.
     */
    public void draw(Graphics2D g2) {
        toPlay.drawAnimation(g2);
        animationRegistryReset(toPlay);
    }

    protected abstract void resetCoordinates();

    protected abstract void introStateUpdate();

    protected abstract void fightStateUpdate();

    protected abstract void endStateUpdate();
    /**
     * Handles logic when the entity is hit by an attack.
     */
    protected void onHit() {
        setCurrentEntityState(EntityStates.HIT_STUN);
        hitStunFrames++;
        toPlay = onHit;
        int HIT_STUN_FRAME_MAX = 20;
        if (hitStunFrames > HIT_STUN_FRAME_MAX) {
            hitStunFrames = 0;
            if(isOutOfStaminaMode) {
                setCurrentEntityState(EntityStates.OUT_OF_STAMINA);
            } else {
                setCurrentEntityState(EntityStates.IDLE);
            }
        }
    }
    /**
     * Enumerates the possible states for the entity.
     */
    protected enum EntityStates {
        IDLE,
        DODGE_RIGHT,
        DODGE_LEFT,
        JAB_RIGHT,
        JAB_LEFT,
        STRONG_PUNCH_RIGHT,
        STRONG_PUNCH_LEFT,
        BLOCK,
        DODGE_DOWN,
        OUT_OF_STAMINA,
        HIT_STUN,
        KNOCKED_OUT;
    }
}
