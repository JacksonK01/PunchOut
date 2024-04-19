package entity;

import entity.animation.Animation;
import event.EventStates;
import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//The mother class for all entities
public abstract class Entity {
    protected int health;
    protected int worldX, worldY;
    protected BufferedImage sprite;
    protected ArrayList<Animation> animationRegistry = new ArrayList<>();
    private EventStates eventState = EventStates.INTRO;
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

    public void addAnimationToRegistry(Animation a) {
        animationRegistry.add(a);
    }

    public void animationRegistryReset(Animation dontResetThisAnimation) {
        animationRegistry.forEach(animation -> {
                    if (animation != dontResetThisAnimation) {
                        animation.reset();
                    }
                }
        );
    }

    public void setEventState(EventStates state) {
        this.eventState = state;
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

    public void update() {
        toPlay = idle;
        if (eventState == EventStates.INTRO) {
            introStateUpdate();
        } else if (eventState == EventStates.FIGHT) {
            fightStateUpdate();
            if(isHitStun()) {
                onHit();
            }
        }
    }
    public void draw(Graphics2D g2) {
        toPlay.drawAnimation(g2);
        animationRegistryReset(toPlay);
    }

    protected abstract void introStateUpdate();

    protected abstract void fightStateUpdate();

    public void onHit() {
        setCurrentStateHitStun();
        hitStunFrames++;
        toPlay = onHit;
        if (hitStunFrames > 15) {
            hitStunFrames = 0;
            setCurrentStateIdle();
        }
    };

    private enum EntityStates {
        IDLE,
        ATTACKING,
        HIT_STUN,
        DODGING,
        KNOCKED_OUT;
    }
}
