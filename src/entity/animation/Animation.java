package entity.animation;

import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents an animation sequence for entities in the Punch Out game.
 * Manages the frames, speed, and drawing of the animation.
 */
public class Animation {
    private final BufferedImage[] frames;
    private final int speed;
    private final int animationEndTime;
    private final boolean loop;
    private int x, y;
    private int width, height;

    private int spriteCounter = 0;
    private int currentFrame = 0;
    private boolean isAnimationDone = false;

    //Only if needed
    Entity ownerEntity;

    /**
     * Constructs a new Animation with the specified builder parameters.
     * @param builder The builder containing animation parameters.
     */
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
        if(ownerEntity != null) {
            ownerEntity.addAnimationToRegistry(this);
        }
    }
    /**
     * Draws the animation on the provided graphics context.
     * @param g2 The graphics context.
     */
    public void drawAnimation(Graphics2D g2) {
        if(!isAnimationDone) {
            spriteCounter++;
        }
        if (spriteCounter > speed) {
            currentFrame++;
            if(currentFrame >= frames.length) {
                if(loop) {
                    currentFrame = 0;
                } else {
                    isAnimationDone = true;
                    currentFrame--;
                }
            }
            spriteCounter = 0;
        }

        if(ownerEntity != null) {
            g2.drawImage(this.frames[currentFrame], ownerEntity.getWorldX(), ownerEntity.getWorldY(), null);
        } else {
            g2.drawImage(this.frames[currentFrame], x, y, null);
        }
    }
    /**
     * Sets the frame at the specified index with the given image.
     * @param i The index of the frame.
     * @param image The image to set.
     */
    public void setFrame(int i, BufferedImage image) {
        if (i >= 0 && i < frames.length) {
            this.frames[i] = image;
        }
    }
    /**
     * Sets the current frame index.
     * @param i The index to set.
     */
    public void setCurrentFrameIndex(int i) {
        if (i >= 0 && i < frames.length) {
            this.currentFrame = i;
        }
    }
    /**
     * Sets the position of the animation.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Sets the owner entity of the animation.
     * @param entity The owner entity.
     */
    public void setOwnerEntity(Entity entity) {
        this.ownerEntity = entity;
    }
    /**
     * Sets the position and draws the animation on the provided graphics context.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param g2 The graphics context.
     */
    public void setPosAndDrawAnimation(int x, int y, Graphics2D g2) {
        setPos(x, y);
        drawAnimation(g2);
    }
    /**
     * Gets the length of the animation frames.
     * @return The length of the frames array.
     */
    public int getLength() {
        return this.frames.length;
    }

    public BufferedImage getFrame(int i) {
        if (0 <= i && i < getLength()) {
            return this.frames[i];
        }

        return null;
    }

    public int getCurrentFrameIndex() {
        return currentFrame;
    }

    public BufferedImage[] getFrames() {
        return this.frames;
    }
    /**
     * Resets the animation to its initial state.
     */
    public void reset() {
        this.spriteCounter = 0;
        this.currentFrame = 0;
        this.isAnimationDone = false;
    }
}
