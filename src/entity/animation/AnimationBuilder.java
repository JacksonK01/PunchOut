package entity.animation;

import entity.Entity;
import game.GamePanel;
import utility.UtilityTool;

import java.awt.image.BufferedImage;

public class AnimationBuilder {
    BufferedImage[] frames;
    int speed = 0;
    int animationEndTime = 0;
    boolean loop = false;
    int x, y = 0;
    int width, height = 0;

    Entity ownerEntity;

    public static AnimationBuilder newInstance() {
        return new AnimationBuilder();
    }

    private AnimationBuilder() {};

    public AnimationBuilder setAnimationWithArray(BufferedImage[] a) {
        if (this.ownerEntity != null) {
            UtilityTool.scaleImage(a, ownerEntity.getEntityWidth(), ownerEntity.getEntityHeight());
        }
        this.frames = a;
        return this;
    }

    public AnimationBuilder setAnimationWithoutArray(int totalAmountOfFrames) {
        this.frames = new BufferedImage[totalAmountOfFrames];
        return this;
    }

    //Don't set a frame before using setting the animation with or without an array
    public AnimationBuilder setFrame(BufferedImage image, int i) {
        BufferedImage scaleImage = ownerEntity != null ? UtilityTool.scaleImage(image, ownerEntity.getEntityWidth(), ownerEntity.getEntityHeight()) : UtilityTool.scaleImage(image, GamePanel.scaledTileSize, GamePanel.scaledTileSize*2);
        if(this.frames != null) {
            if(0 <= i && i < frames.length) {
                frames[i] = scaleImage;
            }
        }
        return this;
    }
    public AnimationBuilder setFrameAndSize(BufferedImage image, int width, int height, int i) {
        BufferedImage scaleImage = UtilityTool.scaleImage(image, width, height);
        if(this.frames != null) {
            if(0 <= i && i < frames.length) {
                frames[i] = scaleImage;
            }
        }
        return this;
    }

    public AnimationBuilder setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public AnimationBuilder setAnimationDuration(int animationEndTime) {
        this.animationEndTime = animationEndTime;
        return this;
    }

    public AnimationBuilder setLoop(boolean isLoop) {
        this.loop = isLoop;
        return this;
    }

    public AnimationBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public AnimationBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public AnimationBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public AnimationBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public AnimationBuilder setOwnerEntity(Entity entity) {
        this.ownerEntity = entity;
        return this;
    }

    public Animation build() {
        return new Animation(this);
    }
}
