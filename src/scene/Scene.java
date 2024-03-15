package scene;

import java.awt.image.BufferedImage;

//TODO add more to this class
public class Scene {
    //TODO make this an array so it can be animated
    private BufferedImage frame;

    public Scene(BufferedImage frame) {
        this.frame = frame;
    }

    public BufferedImage getFrame() {
        return frame;
    }
}
