package game.events;

import entity.Entity;

@FunctionalInterface
public interface RequestHandler<E> {
    E request(Entity receiver);
}
