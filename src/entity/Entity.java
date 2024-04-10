package entity;

import entity.animation.Animation;
import event.EventStates;
import gamepanel.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//The mother class for all entities
public abstract class Entity {
    protected int health;
    protected int worldX, worldY;
    protected BufferedImage sprite;
    protected ArrayList<Animation> animationRegistry = new ArrayList<>();
    protected EventStates eventState = EventStates.INTRO;

    protected boolean isAttacking;
    protected boolean isDodging;

    //This is used for objects like Animation
    protected int entityWidth;
    protected int entityHeight;

    protected final int PLAYER_X_REST_POINT = GamePanel.screenWidth /2 - GamePanel.scaledTileSize /2;
    protected final int PLAYER_Y_REST_POINT = 400;

    protected Animation idle;
    protected Animation toPlay;

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

    public void update() {
        toPlay = idle;
        if (eventState == EventStates.INTRO) {
            introStateUpdate();
        } else if (eventState == EventStates.FIGHT) {
            fightStateUpdate();
        }
    }
    public void draw(Graphics2D g2) {
        toPlay.drawAnimation(g2);
        animationRegistryReset(toPlay);
    }

    public abstract void introStateUpdate();

    public abstract void fightStateUpdate();
}
