package game.events;
/**
 * Represents an event handler for the Punch Out game.
 * Manages the event for entities.
 */
@FunctionalInterface
public interface EventHandler {
    void execute();
}
