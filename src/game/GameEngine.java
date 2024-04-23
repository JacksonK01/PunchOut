package game;

import entity.Entity;
import entity.GlassJoe;
import entity.Player;
import game.events.EventHandler;
import input.KeyHandler;

import java.awt.*;

/**
 * Manages the game engine logic for the Punch Out game.
 * Responsible for handling player and opponent actions, updating game state, and drawing game entities.
 */
public class GameEngine {
    private Player player;
    private Entity opponent;
    private KeyHandler keyH;
    private final GamePhaseManager gamePhaseManager;

    /** Event handler for generic attacks. */
    private final EventHandler genericAttackEvent = (attacker, damage) -> {
        Entity defender = attacker == player ? opponent : player;
        if (defender.isIdle() && !defender.isHitStun()) {
            defender.doDamage(damage);
            defender.setCurrentStateHitStun();
            System.out.println(attacker + " attacked " + defender + " for " + damage + " damage");
        }
    };
    /**
     * Constructs a new GameEngine with the specified game panel.
     * Initializes the player, opponent, key handler, and event manager.
     */
    public GameEngine() {
        this.keyH = new KeyHandler();
        this.player = new Player(keyH, genericAttackEvent);
        this.opponent = new GlassJoe();
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