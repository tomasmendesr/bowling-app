package jobsity.core.services.game;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;

import java.util.List;

public interface GameService {

    /**
     * Returns true if all the players has a score
     * @return
     */
    Boolean gameHasFinished();

    void setFinalScores();

    List<List<Frame>> getFramesFromPlayers(final List<Player> players);
    boolean didPlayerFinish(final List<Frame> playerFrames);
}
