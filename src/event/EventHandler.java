package event;

import entity.Entity;

@FunctionalInterface
public interface EventHandler {
    void execute(Entity owner, int damage);
}
