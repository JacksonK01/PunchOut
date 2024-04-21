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
    protected int health;
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
    int hitStunFrames = 0;
    protected Animation onHit;

    public Entity() {}

    public int getWorldX() {
        return this.worldX;
    }

    public int getWorldY() {
        return this.worldY;
    }

    public int getEntityWidth() {
        return entityWidth;
    }

    public int getEntityHeight() {
        return entityHeight;
    }

    public int getHealth() {
        return this.health;
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
    public void animationRegistryReset(Animation dontResetThisAnimation) {
        animationRegistry.forEach(animation -> {
                    if (animation != dontResetThisAnimation) {
                        animation.reset();
                    }
                }
        );
    }

    public void setCurrentStateIdle() {
        this.currentState = EntityStates.IDLE;
    }

    public void setCurrentStateAttacking() {
        this.currentState = EntityStates.ATTACKING;
    }

    public void setCurrentStateHitStun() {
        this.currentState = EntityStates.HIT_STUN;
    }

    public void setCurrentStateDodging() {
        this.currentState = EntityStates.DODGING;
    }

    public boolean isIdle() {
        return this.currentState == EntityStates.IDLE;
    }

    public boolean isAttacking() {
        return this.currentState == EntityStates.ATTACKING;
    }

    public boolean isDodging() {
        return this.currentState == EntityStates.DODGING;
    }

    public boolean isHitStun() {
        return this.currentState == EntityStates.HIT_STUN;
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
            fightStateUpdate();
            if(isHitStun()) {
                onHit();
            }
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

    protected abstract void introStateUpdate();

    protected abstract void fightStateUpdate();
    /**
     * Handles logic when the entity is hit by an attack.
     */
    public void onHit() {
        setCurrentStateHitStun();
        hitStunFrames++;
        toPlay = onHit;
        if (hitStunFrames > 30) {
            hitStunFrames = 0;
            setCurrentStateIdle();
        }
    }
    /**
     * Enumerates the possible states for the entity.
     */
    private enum EntityStates {
        IDLE,
        ATTACKING,
        HIT_STUN,
        DODGING,
        KNOCKED_OUT;
    }
}
