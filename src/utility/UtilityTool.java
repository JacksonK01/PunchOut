package utility;

import entity.animation.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
    /**
     * Scales the image to the specified width and height.
     * @param original The original image.
     * @param width The width of the scaled image.
     * @param height The height of the scaled image.
     * @return The scaled image.
     */
    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {

        BufferedImage scaleImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaleImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaleImage;
    }
    /**
     * Scales the image to the specified width and height.
     * @param original The original image.
     * @param width The width of the scaled image.
     * @param height The height of the scaled image.
     */
    public static void scaleImage(BufferedImage[] original, int width, int height) {
        for(int i = 0; i < original.length; i++) {
            BufferedImage scaleImage = new BufferedImage(width, height, original[i].getType());
            Graphics2D g2 = scaleImage.createGraphics();
            g2.drawImage(original[i], 0, 0, width, height, null);
            original[i] = scaleImage;
            g2.dispose();

        }
    }

    //This method is a very specific use, which is when the sprite sheet has many sprites in a row, and this follows the common pattern for grabbing the sprites
    //This does not span the y axis.
    /**
     * Creates an array of images for an animation from a sprite sheet.
     * @param spriteSheet The sprite sheet.
     * @param sizeOfArr The size of the array.
     * @param startX The starting x coordinate.
     * @param startY The starting y coordinate.
     * @param xWidth The width of the sprite.
     * @param yHeight The height of the sprite.
     * @param scaleWidth The width of the scaled sprite.
     * @param scaleHeight The height of the scaled sprite.
     * @return The array of images for the animation.
     */
    public static BufferedImage[] createArrayForAnimation(BufferedImage spriteSheet, int sizeOfArr, int startX, int startY, int xWidth, int yHeight, int scaleWidth, int scaleHeight) {
        BufferedImage[] arr = new BufferedImage[sizeOfArr];
        int xOffset = 0;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = spriteSheet.getSubimage(startX + (xOffset * xWidth), startY, xWidth, yHeight);
            startX++;
            xOffset++;
        }
        scaleImage(arr, scaleWidth, scaleHeight);
        return arr;
    }
    /**
     * Flips the images horizontally in an array
     * @param toFlip The image to flip.
     * @return The flipped image.
     */
    public static BufferedImage[] flipImageArray(BufferedImage[] toFlip) {
        BufferedImage[] temp = toFlip.clone();
        for (int i = 0; i < temp.length; i++) {
            temp[i] = flipImageHorizontal(temp[i]);
        }
        return temp;
    }
    /**
     * Flips the image horizontally.
     * @param image The image to flip.
     * @return The flipped image.
     */
    public static BufferedImage flipImageHorizontal(BufferedImage image) {
        BufferedImage copyOfImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        //r = y, c = x
        for(int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {
                copyOfImage.setRGB(c, r, image.getRGB(image.getWidth() - 1 - c, r));
            }
        }
        return copyOfImage;
    }
}
