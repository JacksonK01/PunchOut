package game.events;

import entity.Entity;
/**
 * Represents an attack handler for entities in the Punch Out game.
 * Manages the attack event for entities.
 */
@FunctionalInterface
public interface AttackHandler {
    void execute(Entity owner, int damage);
}
