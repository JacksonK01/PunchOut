package event;

public enum EventStates {
    INTRO(450),
    FIGHT(-1),
    DAMAGED(-1),
    VICTORY(-1),
    LOST(-1),
    KNOCKOUT(-1);

    private final int eventDuration;

    EventStates(int timer) {
        this.eventDuration = timer;
    }

    public int getEventDuration() {
        return this.eventDuration;
    }
}
