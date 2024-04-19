package event;

import game.GamePanel;

public class EventManager {
    private static EventStates globalEventState;
    private static int timer;

    public EventManager() {
        globalEventState = EventStates.INTRO;
        timer = 0;
    }

    private void setGlobalEventState() {
    }

    private void runIntroEvent() {
        timer++;
        if (timer >= EventStates.INTRO.getEventDuration()) {
            globalEventState = EventStates.FIGHT;
        }
    }

    public void update() {
        setGlobalEventState();
        if (globalEventState == EventStates.INTRO) {
            runIntroEvent();
        }
    }

    public static EventStates getGlobalEventState() {
        return globalEventState;
    }
}
