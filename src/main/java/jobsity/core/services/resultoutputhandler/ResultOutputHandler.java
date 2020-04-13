package jobsity.core.services.resultoutputhandler;

import jobsity.core.entities.Frame;

import java.util.List;

public interface ResultOutputHandler {

    /**
     * Prints the game result
     */
    void printGameResult();

    /**
     * Validate if the game has finished and set all scores.
     */
    void finishGame();

    /**
     * Gets the output result of a frame
     * @param frame
     * @return
     */
    String getFramePinfallsOutput(final Frame frame);

    /**
     * Gets the output result of the last frame
     * @param frames
     * @return
     */
    String getLastFrameOutput(final List<Frame> frames);
}
