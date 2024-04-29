package game;

import entity.Entity;
import entity.GlassJoe;
import entity.Player;
import game.events.AttackHandler;
import game.events.EventHandler;
import game.events.EventQueueHandler;
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
    private final Queue<EventHandler> oneFrameEventQueue = new LinkedList<>();

    /** Event handler for generic attacks. */
    private final AttackHandler genericAttackEvent = (attacker, damage) -> {
        Entity defender = attacker == player ? opponent : player;
        if ((defender.isIdle() || defender.isAttacking()) && !defender.isHitStun()) {
            if (attacker == player) {
                player.score += 100;
            }
            defender.doDamage(damage);
            defender.setStateToHit();
            System.out.println(attacker + " attacked " + defender + " for " + damage + " damage");
        }
    };

    private final EventQueueHandler addEventToQueue = (event) -> {
        this.oneFrameEventQueue.add(event);
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


    /**
     * Constructs a new GameEngine with the specified game panel.
     * Initializes the player, opponent, key handler, and event manager.
     */
    public GameEngine() {
        this.keyH = new KeyHandler();
        this.player = new Player(keyH, genericAttackEvent, isRightHandRequest);
        this.opponent = new GlassJoe(genericAttackEvent, isIdleRequest, isHitStunRequest, isAttackingRequest, isDodgingRequest, isHitStunRequest, isStrongRequest);
        this.gamePhaseManager = new GamePhaseManager();
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

        if (oneFrameEventQueue.peek() != null) {
            oneFrameEventQueue.remove().execute();
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
