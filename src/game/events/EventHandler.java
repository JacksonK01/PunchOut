package game.events;

import entity.Entity;

@FunctionalInterface
public interface EventHandler {
    void execute(Entity owner, int damage);
}
