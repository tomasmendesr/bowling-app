package jobsity.core.utils.inputreadervalidator;

public interface FrameLineValidator {

    /**
     * Validates a frame input as string.
     * This throws exception in case of error.
     *
     * @param frameLine
     */
    void validateFrameLine(final String frameLine);
}
