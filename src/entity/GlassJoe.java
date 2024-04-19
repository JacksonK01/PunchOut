package entity;

import entity.animation.Animation;
import entity.animation.AnimationBuilder;
import utility.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GlassJoe extends Entity {
    private final Animation startPose;
    private final Animation walk;

    private final int X_REST_POINT = PLAYER_X_REST_POINT + 4;
    private final int Y_REST_POINT = PLAYER_Y_REST_POINT - 150;

    private int introTimer = 0;

    public GlassJoe() {
        BufferedImage spriteSheet = null;
        try {
            spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/glass_joe/glassjoe.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sprite = spriteSheet.getSubimage(0, 0, 32, 112);
        this.worldX = 525;
        this.worldY = 60;

        this.entityWidth = 80;
        this.entityHeight = 210;

        walk = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(UtilityTool.createArrayForAnimation(spriteSheet, 6, 0, 0, 32, 112, this.entityWidth, this.entityHeight))
                .setSpeed(10)
                .setLoop(true)
                .build();

        startPose = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(1)
                .setFrame(spriteSheet.getSubimage(199, 2, 32, 113), 0)
                .setSpeed(0)
                .build();

        BufferedImage[] temp = UtilityTool.createArrayForAnimation(spriteSheet, 3, 100, 117, 32, 100, this.entityWidth, this.entityHeight);

        this.idle = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(4)
                .setFrame(temp[0], 0)
                .setFrame(temp[1], 1)
                .setFrame(temp[2], 2)
                .setFrame(temp[1], 3)
                .setSpeed(10)
                .setLoop(true)
                .build();

        this.onHit = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(1)
                .setFrame(spriteSheet.getSubimage(265, 114, 32, 100), 0)
                .setSpeed(0)
                .setLoop(false)
                .build();

        this.toPlay = startPose;

    }
    public void updateIntro() {
        if (worldX > X_REST_POINT) {
            worldX--;
        }
        if (worldY < Y_REST_POINT) {
            worldY++;
        }
        if (worldX == X_REST_POINT && worldY == Y_REST_POINT) {
            introTimer = 0;
        }
    }

    @Override
    public void introStateUpdate() {
            if (introTimer < 120) {
                toPlay = startPose;
            } else {
                toPlay = walk;
                updateIntro();
            }
            introTimer++;
        }

    @Override
    public void fightStateUpdate() {

    }

    @Override
    public String toString() {
        return "Glass Joe";
    }
}
