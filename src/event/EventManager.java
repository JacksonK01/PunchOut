package event;

import gamepanel.GamePanel;

public class EventManager {
    private EventStates currentState;
    private GamePanel gp;
    private int timer;

    public EventManager(GamePanel gp) {
        this.currentState = EventStates.INTRO;
        this.gp = gp;
        timer = 0;

        gp.player.setEventState(currentState);
    }

    private void setGlobalEventState() {
        gp.player.setEventState(currentState);
        gp.opponent.setEventState(currentState);
    }

    private void runIntroEvent() {
        timer++;
        if (timer >= EventStates.INTRO.getEventDuration()) {
            this.currentState = EventStates.FIGHT;
        }
    }

    public void update() {
        setGlobalEventState();
        if (currentState == EventStates.INTRO) {
            runIntroEvent();
        }
    }
}
