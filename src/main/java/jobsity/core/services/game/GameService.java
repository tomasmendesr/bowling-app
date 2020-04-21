package jobsity.core.services.game;

import jobsity.core.entities.Frame;

import java.util.List;

public interface GameService {

    /**
     * Returns true if all the players has finished
     * @return
     */
    Boolean gameHasFinished();

    /**
     * Sets the score for all players
     */
    void setFinalScores();

    /**
     * Returns true
     * @param playerFrames
     * @return
     */
    boolean areFramesComplete(final List<Frame> playerFrames);

    /**
     * Calculates the score for a complete list of frames and persist it in the db.
     * @param frames
     */
    void calculateScoreForFrames(List<Frame> frames);
}
