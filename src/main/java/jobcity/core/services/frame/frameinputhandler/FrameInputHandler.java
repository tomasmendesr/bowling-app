package jobcity.core.services.frame.frameinputhandler;

public interface FrameInputHandler {

    /**
     * Receives a frame and process it.
     * @param frameInput
     * @return
     *
     */
    void handle(final String frameInput);

    void validateInput(final String input);

    String getPlayerNameFromInput(final String input);

    int getPinfallsFromInput(final String input);

    String[] splitInput(final String input);
}
