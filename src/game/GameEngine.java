package game;

import entity.Entity;
import entity.GlassJoe;
import entity.Player;
import event.EventHandler;

import java.awt.*;

public class GameEngine {
    GamePanel gp;
    public Player player;
    public Entity opponent;

    private final EventHandler genericAttackEvent = (attacker, damage) -> {
        Entity defender = attacker == player ? opponent : player;
        if (defender.isIdle() && !defender.isHitStun()) {
            defender.doDamage(damage);
            defender.setCurrentStateHitStun();
            System.out.println(attacker + " attacked " + defender + " for " + damage + " damage");
        }
    };

    public GameEngine(GamePanel gp) {
        this.gp = gp;

        player = new Player(gp, genericAttackEvent);
        opponent = new GlassJoe();
    }

    public void update() {
        player.update();
        opponent.update();
    }

    public void draw(Graphics2D g2) {
        opponent.draw(g2);
        player.draw(g2);
    }
}
