package entity;

import java.awt.image.BufferedImage;

//The mother class for all entities
public abstract class Entity {
    int health;
    int worldX, worldY;
    BufferedImage sprite;

    public Entity() {

    }
}
