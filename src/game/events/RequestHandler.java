package game.events;

import entity.Entity;
/**
 * Represents a request handler for entities in the Punch Out game.
 * Manages the request event for entities.
 */
@FunctionalInterface
public interface RequestHandler<E> {
    E request(Entity receiver);
}
