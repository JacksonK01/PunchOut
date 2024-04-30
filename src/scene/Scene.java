package scene;

import java.awt.image.BufferedImage;

public class Scene {
    private BufferedImage frame;
    /**
     * Constructs a new scene with the provided frame.
     * @param frame The frame for the scene.
     */
    public Scene(BufferedImage frame) {
        this.frame = frame;
    }
    /**
     * Retrieves the frame associated with this scene.
     * @return The frame.
     */
    public BufferedImage getFrame() {
        return frame;
    }
}
