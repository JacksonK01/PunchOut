package game;

import game.GamePhase;

public class GamePhaseManager {
    private static GamePhase globalEventState;
    private static int timer;

    public GamePhaseManager() {
        globalEventState = GamePhase.INTRO;
        timer = 0;
    }

    private void setGlobalEventState() {
    }

    private void runIntroEvent() {
        timer++;
        if (timer >= GamePhase.INTRO.getEventDuration()) {
            globalEventState = GamePhase.FIGHT;
        }
    }

    public void update() {
        setGlobalEventState();
        if (globalEventState == GamePhase.INTRO) {
            runIntroEvent();
        }
    }

    public static GamePhase getGlobalEventState() {
        return globalEventState;
    }

    public static int getTimeLeft() {
        if (globalEventState.doesEventHaveTimer()) {
            return globalEventState.getEventDuration() - timer;
        } else {
            return -1;
        }
    }
}
