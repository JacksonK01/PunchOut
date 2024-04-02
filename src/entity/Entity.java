package entity;

import entity.animation.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//The mother class for all entities
public abstract class Entity {
    int health;
    int worldX, worldY;
    BufferedImage sprite;
    protected ArrayList<Animation> animationRegistry = new ArrayList<>();

    boolean isAttacking;
    boolean isDodging;

    //This is used for objects like Animation
    int entityWidth;
    int entityHeight;

    public Entity() {

    }

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

    public void addAnimationToRegistry(Animation a) {
        animationRegistry.add(a);
    }

    public void animationRegistryReset() {
        animationRegistry.forEach(animation -> {
                animation.reset();
            }
        );
    }

    public abstract void update();
    public abstract void draw(Graphics2D g2);
}
