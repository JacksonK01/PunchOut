package entity.animation;

import entity.Entity;
import game.GamePanel;
import utility.UtilityTool;

import java.awt.image.BufferedImage;

/**
 * Builder class for creating Animation objects.
 * Allows setting various parameters to customize the animation.
 */
public class AnimationBuilder {
    BufferedImage[] frames;
    int speed = 0;
    int animationEndTime = 0;
    boolean loop = false;
    int x, y = 0;
    int width, height = 0;

    Entity ownerEntity;

    /**
     * Static factory method to create a new instance of AnimationBuilder.
     * @return A new instance of AnimationBuilder.
     */
    public static AnimationBuilder newInstance() {
        return new AnimationBuilder();
    }

    private AnimationBuilder() {};
    /**
     * Sets the animation frames using an array of BufferedImages. This method should not be used if you've already used setAnimationWithoutArray().
     * @param a The array of BufferedImages representing animation frames.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setAnimationWithArray(BufferedImage[] a) {
        if (this.ownerEntity != null) {
            UtilityTool.scaleImage(a, ownerEntity.getEntityWidth(), ownerEntity.getEntityHeight());
        }
        this.frames = a.clone();
        return this;
    }
    /**
     * Sets the total amount of frames for the animation. This method should not be used if you've already used setAnimationWithArray().
     * @param totalAmountOfFrames The total number of frames.
     * @return The AnimationBuilder instance for method chaining.
     */
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
    /**
     * Sets the frame and size of the animation. This method should not be used if you've already used setAnimationWithArray().
     * @param image The image to set as the frame.
     * @param width The width of the frame.
     * @param height The height of the frame.
     * @param i The index of the frame.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setFrameAndSize(BufferedImage image, int width, int height, int i) {
        BufferedImage scaleImage = UtilityTool.scaleImage(image, width, height);
        if(this.frames != null) {
            if(0 <= i && i < frames.length) {
                frames[i] = scaleImage;
            }
        }
        return this;
    }
    /**
     * Sets the speed of the animation.
     * @param speed The speed of the animation.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setSpeed(int speed) {
        this.speed = speed;
        return this;
    }
    /**
     * Sets the duration of the animation.
     * @param animationEndTime The end time of the animation.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setAnimationDuration(int animationEndTime) {
        this.animationEndTime = animationEndTime;
        return this;
    }
    /**
     * Sets whether the animation should loop.
     * @param isLoop True if the animation should loop, false otherwise.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setLoop(boolean isLoop) {
        this.loop = isLoop;
        return this;
    }
    /**
     * Sets the x position of the animation.
     * @param x The x position of the animation.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setX(int x) {
        this.x = x;
        return this;
    }
    /**
     * Sets the y position of the animation.
     * @param y The y position of the animation.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setY(int y) {
        this.y = y;
        return this;
    }
    /**
     * Sets the width of the animation.
     * @param width The width of the animation.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setWidth(int width) {
        this.width = width;
        return this;
    }
    /**
     * Sets the height of the animation.
     * @param height The height of the animation.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setHeight(int height) {
        this.height = height;
        return this;
    }
    /**
     * Sets the owner for this animation.
     * @return The AnimationBuilder instance for method chaining.
     */
    public AnimationBuilder setOwnerEntity(Entity entity) {
        this.ownerEntity = entity;
        return this;
    }
    /**
     * Builds and returns the Animation object based on the provided parameters.
     * @return The created Animation object.
     */
    public Animation build() {
        return new Animation(this);
    }
}
