package jobcity.core.services.game;

import jobcity.core.entities.Frame;
import jobcity.core.entities.Player;

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
