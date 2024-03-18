package scene;

import gamepanel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SceneRenderer {
    GamePanel gp;
    BufferedImage spriteSheet;
    Scene blueBoxingRing;

    public SceneRenderer(GamePanel gp) {
        this.gp = gp;
        try {
            this.spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/scenes/boxing_ring/boxing_rings2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        blueBoxingRing = new Scene(spriteSheet.getSubimage(2, 2, 256, 224));
    }

    //TODO make this streamlined
    public void draw(Graphics g2) {
        g2.drawImage(blueBoxingRing.getFrame(), 0, 0, gp.screenWidth, gp.screenHeight, null);
    }
}
