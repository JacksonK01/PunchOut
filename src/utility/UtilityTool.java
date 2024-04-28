package utility;

import entity.animation.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {

        BufferedImage scaleImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaleImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaleImage;
    }

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

    public static BufferedImage[] flipImageArray(BufferedImage[] toFlip) {
        BufferedImage[] temp = toFlip.clone();
        for (int i = 0; i < temp.length; i++) {
            temp[i] = flipImageHorizontal(temp[i]);
        }
        return temp;
    }

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
