package game.events;

import entity.Entity;

@FunctionalInterface
public interface AttackHandler {
    void execute(Entity owner, int damage);
}
