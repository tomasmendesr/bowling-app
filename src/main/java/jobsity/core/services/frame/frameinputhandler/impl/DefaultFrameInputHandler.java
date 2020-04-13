package jobsity.core.services.frame.frameinputhandler.impl;

import jobsity.core.services.frame.FrameService;
import jobsity.core.services.frame.frameinputhandler.FrameInputHandler;
import jobsity.core.utils.inputreadervalidator.FrameLineValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultFrameInputHandler implements FrameInputHandler {

    private FrameLineValidator frameLineValidator;
    private FrameService frameService;

    @Autowired
    public DefaultFrameInputHandler(FrameLineValidator frameLineValidator, FrameService frameService) {
        this.frameLineValidator = frameLineValidator;
        this.frameService = frameService;
    }

    @Override
    public void handle(final String frameInput) {
        validateInput(frameInput);
        frameService.handleFrame(getPlayerNameFromInput(frameInput), getPinfallsFromInput(frameInput));
    }

    @Override
    public void validateInput(final String input) {
        frameLineValidator.validateFrameLine(input);
    }

    @Override
    public String getPlayerNameFromInput(final String input) {
        final String[] splitedLine = splitInput(input);
        return splitedLine[0];
    }

    @Override
    public int getPinfallsFromInput(final String input) {
        final String[] splitedLine = splitInput(input);
        if (!"F".equals(splitedLine[1])) {
            return Integer.parseInt(splitedLine[1]);
        }
        return 0;
    }

    @Override
    public String[] splitInput(final String input) {
        return input.split(" ");
    }
}
