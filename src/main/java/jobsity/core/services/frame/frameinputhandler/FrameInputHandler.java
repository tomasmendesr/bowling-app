package jobsity.core.services.frame.frameinputhandler;

public interface FrameInputHandler {

    /**
     * Receives a frame and process it.
     * @param frameInput
     * @return
     *
     */
    void handle(final String frameInput);

    /**
     * Validate the input using FrameLineValidator
     * @param input
     * @throws jobsity.core.exceptions.LineValidationException
     */
    void validateInput(final String input);

    /**
     * Splits the input and obtains the player name
     * @param input
     * @return
     */
    String getPlayerNameFromInput(final String input);

    /**
     * Splits the input and obtains the pinfalls number
     * @param input
     * @return
     */
    int getPinfallsFromInput(final String input);

    /**
     * Split an input by a space
     * @param input
     * @return
     */
    String[] splitInput(final String input);
}
