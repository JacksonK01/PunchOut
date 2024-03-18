package entity.animation;

import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {
    BufferedImage[] frames;
    private final int speed;
    private int animationEndTime;
    private final boolean loop;
    private int x, y;
    private int width, height;

    private int spriteCounter = 0;
    private int currentFrame = 0;
    private boolean isAnimationDone = false;

    //Only if needed
    Entity ownerEntity;

    public Animation(AnimationBuilder builder) {
        this.frames = builder.frames;
        this.speed = builder.speed;
        this.animationEndTime = builder.animationEndTime;
        this.loop = builder.loop;
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.ownerEntity = builder.ownerEntity;
    }

    public void drawAnimation(Graphics2D g2) {
        if(!isAnimationDone) {
            spriteCounter++;
        }
        if (spriteCounter > speed) {
            currentFrame++;
            if(currentFrame == frames.length - 1) {
                isAnimationDone = true;
            }
            spriteCounter = 0;
        }
        if(ownerEntity != null) {
            g2.drawImage(this.frames[currentFrame], ownerEntity.getWorldX(), ownerEntity.getWorldY(), ownerEntity.getEntityWidth(), ownerEntity.getEntityHeight(), null);
        } else {
            g2.drawImage(this.frames[currentFrame], x, y, width, height, null);
        }

        if(loop) {
            isAnimationDone = false;
        }
    }

    public BufferedImage getFrame(int i) {
        return frames[i];
    }

    public void setFrame(int i, BufferedImage image) {
        if (i >= 0 && i < frames.length) {
            this.frames[i] = image;
        }
    }

    public void setCurrentFrameIndex(int i) {
        if (i >= 0 && i < frames.length) {
            this.currentFrame = i;
        }
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setOwnerEntity(Entity entity) {
        this.ownerEntity = entity;
    }

    public void setPosAndDrawAnimation(int x, int y, Graphics2D g2) {
        setPos(x, y);
        drawAnimation(g2);
    }

    //I'd really like to find a natural way to end the animation
    public void reset() {
        this.spriteCounter = 0;
        this.currentFrame = 0;
        this.isAnimationDone = false;
    }
}
