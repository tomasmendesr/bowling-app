package jobsity.core.services.resultoutputhandler;

import jobsity.core.entities.Frame;

import java.util.List;

public interface ResultOutputHandler {
    void printGameResult();
    void finishGame();
    String getFramePinfallsOutput(final Frame frame);
    String getLastFrameOutput(final List<Frame> frames);
}
