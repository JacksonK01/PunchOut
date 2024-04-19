package game;

import entity.Entity;
import entity.GlassJoe;
import entity.Player;
import event.EventHandler;
import event.EventManager;
import input.KeyHandler;

import java.awt.*;
import java.security.Key;

public class GameEngine {
    GamePanel gp;
    private Player player;
    private Entity opponent;
    private KeyHandler keyH;
    private final EventManager eventManager;

    private final EventHandler genericAttackEvent = (attacker, damage) -> {
        Entity defender = attacker == player ? opponent : player;
        if (defender.isIdle() && !defender.isHitStun()) {
            defender.doDamage(damage);
            defender.setCurrentStateHitStun();
            System.out.println(attacker + " attacked " + defender + " for " + damage + " damage");
        }
    };

    public GameEngine(GamePanel gp) {
        this.keyH = new KeyHandler();
        this.gp = gp;
        this.player = new Player(keyH, genericAttackEvent);
        this.opponent = new GlassJoe();
        this.eventManager = new EventManager();
    }

    public Player getPlayer() {
        return this.player;
    }

    public Entity getOpponent() {
        return this.opponent;
    }

    public KeyHandler getKeyHandler() {
        return this.keyH;
    }

    public void update() {
        eventManager.update();
        player.update();
        opponent.update();
    }

    public void draw(Graphics2D g2) {
        opponent.draw(g2);
        player.draw(g2);
    }
}
