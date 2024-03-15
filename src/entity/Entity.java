package entity;

import java.awt.image.BufferedImage;

//The mother class for all entities
public abstract class Entity {
    int health;
    int worldX, worldY;
    BufferedImage sprite;

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
}
