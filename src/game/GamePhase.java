package game;

public enum GamePhase {
    INTRO(450),
    FIGHT(-1),
    END(-1);

    private final int eventDuration;

    GamePhase(int timer) {
        this.eventDuration = timer;
    }

    public int getEventDuration() {
        return this.eventDuration;
    }

    public boolean doesEventHaveTimer() {
        return getEventDuration() >= 0;
    }
}
