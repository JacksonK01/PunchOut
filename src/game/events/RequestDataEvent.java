package game.events;

import entity.Entity;

@FunctionalInterface
public interface RequestDataEvent<T> {
    T execute(Entity owner);
}