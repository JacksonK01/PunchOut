package entity;

import entity.animation.Animation;
import entity.animation.AnimationBuilder;
import game.events.EventHandler;
import game.events.RequestHandler;
import utility.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GlassJoe extends Entity {
    private final Animation startPose;
    private final Animation walk;
    private final Animation jabLeft;
    private final Animation jabRight;
    private final Animation taunt;
    private final Animation strongPunchLeftArm;
    private final Animation strongPunchRightArm;
    private final Animation dodgeLeft;
    private final Animation dodgeRight;
    private Animation gettingBackUp;
    private final Animation block;

    EventHandler attackEvent;
    RequestHandler<Boolean> isPlayerIdleRequest;
    RequestHandler<Boolean> isPlayerHitStunRequest;


    private final int X_REST_POINT = PLAYER_X_REST_POINT + 4;
    private final int Y_REST_POINT = PLAYER_Y_REST_POINT - 150;

    private int introTimer = 0;

    public GlassJoe(EventHandler attackEvent, RequestHandler<Boolean> isPlayerIdleRequest, RequestHandler<Boolean> isPlayerHitStunRequest) {
        this.attackEvent = attackEvent;
        this.isPlayerIdleRequest = isPlayerIdleRequest;
        this.isPlayerHitStunRequest = isPlayerHitStunRequest;

        BufferedImage spriteSheet = null;
        try {
            spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/entities/glass_joe/glassjoe.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sprite = spriteSheet.getSubimage(0, 0, 32, 112);
        this.worldX = 525;
        this.worldY = 60;

        this.entityWidth = 80;
        this.entityHeight = 210;

        int SPRITE_WIDTH = 32;
        int SPRITE_HEIGHT = 112;
        int SPRITE_HEIGHT2 = 100;

        walk = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(UtilityTool.createArrayForAnimation(spriteSheet, 6, 0, 0, SPRITE_WIDTH, SPRITE_HEIGHT, this.entityWidth, this.entityHeight))
                .setSpeed(10)
                .setLoop(true)
                .build();

        startPose = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(1)
                .setFrame(spriteSheet.getSubimage(199, 2, 32, 113), 0)
                .setSpeed(0)
                .build();

        BufferedImage[] temp = UtilityTool.createArrayForAnimation(spriteSheet, 3, 100, 117, SPRITE_WIDTH, SPRITE_HEIGHT2, this.entityWidth, this.entityHeight);

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

        int punchSpeed = 12;

        temp = UtilityTool.createArrayForAnimation(spriteSheet, 2, 231, 12, SPRITE_WIDTH, SPRITE_HEIGHT2, this.entityWidth, this.entityHeight);

        this.jabLeft = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(temp)
                .setSpeed(punchSpeed)
                .setLoop(false)
                .build();

        temp = UtilityTool.flipImageArray(jabLeft.getFrames());

        this.jabRight = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(temp)
                .setLoop(false)
                .setSpeed(punchSpeed)
                .build();

        temp = UtilityTool.createArrayForAnimation(spriteSheet, 3, 2, 117, SPRITE_WIDTH, SPRITE_HEIGHT2, this.entityWidth, this.entityHeight);

        this.taunt = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(6)
                .setFrame(temp[2], 0)
                .setFrame(temp[1], 1)
                .setFrame(temp[0], 2)
                .setFrame(temp[2], 3)
                .setFrame(idle.getFrame(1), 4)
                .setFrame(temp[0], 5)
                .setLoop(true)
                .setSpeed(8)
                .build();

        int STRONG_PUNCH_SPEED = 12;

        this.strongPunchLeftArm = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(3)
                .setFrame(spriteSheet.getSubimage(528, 18, SPRITE_WIDTH + 3, SPRITE_HEIGHT2), 0)
                .setFrame(spriteSheet.getSubimage(571, 18, SPRITE_WIDTH + 3, SPRITE_HEIGHT2), 1)
                .setFrame(spriteSheet.getSubimage(614, 12, SPRITE_WIDTH + 7, SPRITE_HEIGHT2), 2)
                .setLoop(false)
                .setSpeed(STRONG_PUNCH_SPEED)
                .build();

        temp = UtilityTool.flipImageArray(strongPunchLeftArm.getFrames());

        this.strongPunchRightArm = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithArray(temp)
                .setLoop(false)
                .setSpeed(STRONG_PUNCH_SPEED)
                .build();

        this.dodgeRight = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(1)
                .setFrame(spriteSheet.getSubimage(366, 12, 38, SPRITE_HEIGHT2), 0)
                .setLoop(false)
                .setSpeed(0)
                .build();

        this.dodgeLeft = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(1)
                .setFrame(UtilityTool.flipImageHorizontal(dodgeRight.getFrame(0)), 0)
                .setLoop(false)
                .setSpeed(0)
                .build();

        this.block = AnimationBuilder.newInstance()
                .setOwnerEntity(this)
                .setAnimationWithoutArray(3)
                .setFrame(spriteSheet.getSubimage(409, 13, SPRITE_WIDTH + 2, SPRITE_HEIGHT2), 0)
                .setFrame(spriteSheet.getSubimage(450, 13, SPRITE_WIDTH + 2, SPRITE_HEIGHT2), 1)
                .setFrame(spriteSheet.getSubimage(491, 13, SPRITE_WIDTH + 2, SPRITE_HEIGHT2), 2)
                .setSpeed(12)
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
        if (isPlayerIdleRequest.request(this) && isReadyForAction()) {
            setCurrentEntityState(EntityStates.JAB_RIGHT);
        }

        if (isJabRight()) {
            this.toPlay = jabRight;
            if (toPlay.isAnimationDone(10)) {
                attackEvent.execute(this, 20);
            }
            if (toPlay.isAnimationDone()) {
                setCurrentEntityState(EntityStates.IDLE);
                addCoolDown(10);
            }
        }

        if (cooldown > 0) {
            cooldown--;
        }
    }

    @Override
    public String toString() {
        return "Glass Joe";
    }
}
