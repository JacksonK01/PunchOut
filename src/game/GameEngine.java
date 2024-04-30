package game;

import entity.Entity;
import entity.GlassJoe;
import entity.Player;
import game.events.AttackHandler;
import game.events.EventHandler;
import game.events.RequestHandler;
import input.KeyHandler;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Manages the game engine logic for the Punch Out game.
 * Responsible for handling player and opponent actions, updating game state, and drawing game entities.
 */
public class GameEngine {
    private Player player;
    private Entity opponent;
    private KeyHandler keyH;
    private final GamePhaseManager gamePhaseManager;
    private int timer = 0;
    Sound crowd = new Sound("/sound/effect/crowd_noise.wav");
    Sound win = new Sound("/sound/music/win_theme.wav");
    Sound lose = new Sound("/sound/music/lost_theme.wav");

    /** Event handler for generic attacks. */
    private final AttackHandler genericAttackEvent = (attacker, damage) -> {
        Entity defender = attacker == player ? opponent : player;
        if (!defender.isDodging() && !defender.isHitStun()) {
            if (attacker == player) {
                player.score += 100;
            } else {
                if (player.score > 0) {
                    player.score -= 100;
                }
            }
            crowd.changeVolume(-24);
            crowd.play();
            timer = 0;
            defender.doDamage(damage);
            defender.setStateToHit();

            if(player.getHealth() <= 0) {
                lose.play();
            } else if (opponent.getHealth() <= 0) {
                win.play();
            }
        }
    };

    private final RequestHandler<Boolean> isDodgingRequest = (receiver) -> {
        Entity sender = receiver == player ? opponent : player;
        return sender.isDodging();
    };
    private final RequestHandler<Boolean> isStrongRequest = (receiver) -> {
        Entity sender = receiver == player ? opponent : player;
        return sender.isStrongPunch();
    };

    private final RequestHandler<Boolean> isAttackingRequest = (receiver) -> {
        Entity sender = receiver == player ? opponent : player;
        return sender.isAttacking();
    };

    private final RequestHandler<Boolean> isHitStunRequest = (receiver) -> {
        Entity sender = receiver == player ? opponent : player;
        return sender.isHitStun();
    };

    private final RequestHandler<Boolean> isIdleRequest = (receiver) -> {
        Entity sender = receiver == player ? opponent : player;
        return sender.isIdle();
    };
    //If this is false, it's safe to assume it's the left hand
    private final RequestHandler<Boolean> isRightHandRequest = (receiver) -> {
        Entity sender = receiver == player ? opponent : player;
        return sender.isRightHand();
    };

    private final RequestHandler<Boolean> isMatchOver = (placeHolder) -> {
        return player.getHealth() <= 0 || opponent.getHealth() <= 0;
    };


    /**
     * Constructs a new GameEngine with the specified game panel.
     * Initializes the player, opponent, key handler, and event manager.
     */
    public GameEngine() {
        this.keyH = new KeyHandler();
        this.player = new Player(keyH, genericAttackEvent, isRightHandRequest);
        this.opponent = new GlassJoe(genericAttackEvent, isIdleRequest, isHitStunRequest, isAttackingRequest, isDodgingRequest, isHitStunRequest, isStrongRequest);
        this.gamePhaseManager = new GamePhaseManager(isMatchOver);

        win.changeVolume(-15);
        lose.changeVolume(-15);
    }
    /**
     * Retrieves the player entity.
     * @return The player entity.
     */
    public Player getPlayer() {
        return this.player;
    }
    /**
     * Retrieves the opponent entity.
     * @return The opponent entity.
     */
    public Entity getOpponent() {
        return this.opponent;
    }
    /**
     * Retrieves the key handler associated with this game engine.
     * @return The key handler.
     */
    public KeyHandler getKeyHandler() {
        return this.keyH;
    }
    /**
     * Updates the game state by updating event manager, player, and opponent.
     */
    public void update() {
        gamePhaseManager.update();
        player.update();
        opponent.update();
        if(crowd.isPlaying()){
            if(timer-24 > -60){
                timer--;
                crowd.changeVolume(timer-24);
            }
        }
    }
    /**
     * Draws the opponent and player entities on the provided graphics context.
     * @param g2 The graphics context.
     */
    public void draw(Graphics2D g2) {
        opponent.draw(g2);
        player.draw(g2);
    }
}
