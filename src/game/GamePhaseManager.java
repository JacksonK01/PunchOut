package game;

import game.GamePhase;
import game.events.RequestHandler;

public class GamePhaseManager {
    private static GamePhase globalEventState;
    private static int timer;
    private static RequestHandler<Boolean> isGameOver;
    /**
     * Constructs a new game phase manager with the provided check game over event.
     * @param isCheckGameOver The check game over event.
     */
    public GamePhaseManager(RequestHandler<Boolean> isCheckGameOver) {
        globalEventState = GamePhase.INTRO;
        timer = 0;
        isGameOver = isCheckGameOver;
    }
    /**
     * Runs the intro event.
     */
    private void runIntroEvent() {
        timer++;
        if (timer >= GamePhase.INTRO.getEventDuration()) {
            globalEventState = GamePhase.FIGHT;
        }
    }
    /**
     * Updates the game phase manager.
     */
    public void update() {
        if (globalEventState == GamePhase.INTRO) {
            runIntroEvent();
        }
        if(isGameOver.request(null)) {
            globalEventState = GamePhase.END;
        }
    }
    /**
     * Retrieves the current global event state.
     * @return The current global event state.
     */
    public static GamePhase getGlobalEventState() {
        return globalEventState;
    }
    /**
     * Retrieves the time left in the current event.
     * @return The time left in the current event.
     */
    public static int getTimeLeft() {
        if (globalEventState.doesEventHaveTimer()) {
            return globalEventState.getEventDuration() - timer;
        } else {
            return -1;
        }
    }
}
