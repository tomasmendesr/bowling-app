package jobcity.core.services.frame.frameinputhandler;

public interface FrameInputHandler {

    /**
     * Receives a frame and process it.
     * @param frameInput
     * @return
     */
    void handle(final String frameInput);
}
