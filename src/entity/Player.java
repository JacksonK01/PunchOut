package entity;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {

    public Player() {
        this.worldX = 200;
        this.worldY = 200;

        try {
            this.sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/little_mac/little_mac_spritesheet.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw() {

    }
}
