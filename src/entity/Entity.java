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
    /**
     * returns if entity is dodging left
     */
    protected boolean isDodgeLeft() {
        return this.currentState == EntityStates.DODGE_LEFT;
    }
    /**
     * returns if entity is Jabbing right
     */
    protected boolean isJabRight() {
        return this.currentState == EntityStates.JAB_RIGHT;
    }
    /**
     * returns if entity is Jabbing left
     */
    protected boolean isJabLeft() {
        return this.currentState == EntityStates.JAB_LEFT;
    }
    /**
     * returns if entity is doing a strong punch
     */
    public boolean isStrongPunch() {
        return this.currentState == EntityStates.STRONG_PUNCH_RIGHT || this.currentState == EntityStates.STRONG_PUNCH_LEFT;
    }
    /**
     * returns if entity is Strong punching right
     */
    protected boolean isStrongPunchRight() {
        return this.currentState == EntityStates.STRONG_PUNCH_RIGHT;
    }
    /**
     * returns if entity is Strong punching left
     */
    protected boolean isStrongPunchLeft() {
        return this.currentState == EntityStates.STRONG_PUNCH_LEFT;
    }
    /**
     * returns if entity is blocking
     */
    protected boolean isBlock() {
        return this.currentState == EntityStates.BLOCK;
    }
    /**
     * returns if entity is dodging down
     */
    protected boolean isDodgeDown() {
        return this.currentState == EntityStates.DODGE_DOWN;
    }
    /**
     * returns if entity is out of stamina
     */
    protected boolean isOutOfStamina() {
        return this.currentState == EntityStates.OUT_OF_STAMINA;
    }
    /**
     * returns if entity is knocked out
     */
    protected boolean isKnockedOut() {
        return this.currentState == EntityStates.KNOCKED_OUT;
    }
    /**
     * returns if entity is idle
     */
    public boolean isIdle() {
        return this.currentState == EntityStates.IDLE;
    }
    /**
     * returns if entity is in hit stun
     */
    public boolean isHitStun() {
        return this.currentState == EntityStates.HIT_STUN;
    }

    /**
     * returns if entity is punching with right hand
     */
    // if false, assume left hand
    public boolean isRightHand() {
        return isStrongPunchRight() || isJabRight();
    }
    /**
     * returns if entity is attacking
     */
    public boolean isAttacking() {
        return this.currentState == EntityStates.JAB_RIGHT ||
                this.currentState == EntityStates.JAB_LEFT ||
                this.currentState == EntityStates.STRONG_PUNCH_RIGHT ||
                this.currentState == EntityStates.STRONG_PUNCH_LEFT;
    }
    /**
     * returns if entity is dodging
     * @return boolean if entity is dodging
     */
    public boolean isDodging() {
        return this.currentState == EntityStates.DODGE_DOWN ||
                this.currentState == EntityStates.BLOCK ||
                this.currentState == EntityStates.DODGE_LEFT ||
                this.currentState == EntityStates.DODGE_RIGHT;
    }
    /**
     * returns if entity is ready for action
     * @return boolean if entity is ready for action
     */
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
    /**
     * Sets the current state of the entity.
     * @param state The state to set the entity to.
     */
    public void setCurrentEntityState(EntityStates state) {
        this.currentState = state;
    }
    /**
     * Adds the given number to the entity's cooldown.
     * @param num The number to add to the cooldown.
     */
    protected void addCoolDown(int num) {
        this.cooldown += num;
    }
    /**
     * Sets the entity's state to hit stun.
     */
    public void setStateToHit() {
        this.currentState = EntityStates.HIT_STUN;
    }
    /**
     * Does damage to the entity.
     * If the entity's health drops to 0 or below, the entity is knocked out.
     */
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
    /**
     * Used differently for each entity to reset coordinates.
     */

    protected abstract void resetCoordinates();
    /**
     * Used differently for each entity to update the entity's state and animations during the intro phase.
     */
    protected abstract void introStateUpdate();
    /**
     * Used differently for each entity to update the entity's state and animations during the fight phase.
     */
    protected abstract void fightStateUpdate();
    /**
     * Used differently for each entity to update the entity's state and animations during the end phase.
     */
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
