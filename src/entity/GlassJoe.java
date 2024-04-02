package entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GlassJoe extends Entity {
    BufferedImage spriteSheet;

    public GlassJoe() {
        try {
            this.spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/glass_joe/glassjoe.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sprite = spriteSheet.getSubimage(0, 0, 32, 112);
        this.worldX = 325;
        this.worldY = 200;

        this.entityWidth = 80;
        this.entityHeight = 200;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(this.sprite, this.worldX, this.worldY, this.entityWidth, this.entityHeight, null);
    }
}
