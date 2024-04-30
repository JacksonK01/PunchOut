package game;
/**
 * Represents the game phase for the Punch Out game.
 */
public enum GamePhase {
    INTRO(450),
    FIGHT(-1),
    END(-1);

    private final int eventDuration;
    /**
     * Constructs a new game phase with the provided timer.
     * @param timer The timer for the game phase.
     */
    GamePhase(int timer) {
        this.eventDuration = timer;
    }
    /**
     * Gets the duration of the event.
     * @return The duration of the event.
     */
    public int getEventDuration() {
        return this.eventDuration;
    }
    /**
     * Determines if the event has a timer.
     * @return True if the event has a timer, false otherwise.
     */
    public boolean doesEventHaveTimer() {
        return getEventDuration() >= 0;
    }
}
