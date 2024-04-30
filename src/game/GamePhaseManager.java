package game;

import game.GamePhase;
import game.events.RequestHandler;

public class GamePhaseManager {
    private static GamePhase globalEventState;
    private static int timer;
    private static RequestHandler<Boolean> isGameOver;

    public GamePhaseManager(RequestHandler<Boolean> isCheckGameOver) {
        globalEventState = GamePhase.INTRO;
        timer = 0;
        isGameOver = isCheckGameOver;
    }

    private void runIntroEvent() {
        timer++;
        if (timer >= GamePhase.INTRO.getEventDuration()) {
            globalEventState = GamePhase.FIGHT;
        }
    }

    public void update() {
        if (globalEventState == GamePhase.INTRO) {
            runIntroEvent();
        }
        if(isGameOver.request(null)) {
            globalEventState = GamePhase.END;
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
