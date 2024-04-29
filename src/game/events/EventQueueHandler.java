package game.events;

@FunctionalInterface
public interface EventQueueHandler {
    void execute(EventHandler toAdd);
}
