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
    private int frameCounter = 0;
    private int currentFrameIndex = 0;
    private int duration = 0;
    private boolean isAnimationDone = false;
    private final Entity ownerEntity; // Only if needed

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

        if (ownerEntity != null) {
            ownerEntity.addAnimationToRegistry(this);
        }
    }

    /**
     * Draws the animation on the provided graphics context.
     * @param g2 The graphics context.
     */
    public void drawAnimation(Graphics2D g2) {
        if (!isAnimationDone) {
            frameCounter++;
            duration++;
        }
        if (frameCounter > speed) {
            if (currentFrameIndex >= frames.length - 1) {
                if (loop) {
                    currentFrameIndex = 0;
                } else {
                    isAnimationDone = true;
                }
            } else {
                currentFrameIndex++;
            }
            frameCounter = 0;
        }

        if (ownerEntity != null) {
            g2.drawImage(this.frames[currentFrameIndex], ownerEntity.getWorldX(), ownerEntity.getWorldY(), null);
        } else {
            g2.drawImage(this.frames[currentFrameIndex], x, y, null);
        }
    }

    // Setters
    public void setFrame(int i, BufferedImage image) {
        if (i >= 0 && i < frames.length) {
            this.frames[i] = image;
        }
    }

    public void setCurrentFrameIndex(int i) {
        if (i >= 0 && i < frames.length) {
            this.currentFrameIndex = i;
        }
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getAmountOfFrames() {
        return this.frames.length;
    }

    public BufferedImage getFrame(int i) {
        if (0 <= i && i < getAmountOfFrames()) {
            return this.frames[i];
        }
        return null;
    }

    public BufferedImage[] getFrames() {
        return this.frames;
    }

    public int getAnimationDuration() {
        return speed * frames.length;
    }

    public boolean isAnimationDone() {
        return duration >= getAnimationDuration();
    }

    public boolean isAnimationDone(int subtract) {
        return duration >= getAnimationDuration() - subtract;
    }

    // Reset
    public void reset() {
        this.frameCounter = 0;
        this.currentFrameIndex = 0;
        this.duration = 0;
        this.isAnimationDone = false;
    }
}
