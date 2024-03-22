package utility;

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
}
