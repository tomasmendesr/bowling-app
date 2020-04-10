package jobcity.core.services.frame.frameinputhandler.impl;

import jobcity.core.services.frame.FrameService;
import jobcity.core.services.frame.frameinputhandler.FrameInputHandler;
import jobcity.core.utils.inputreadervalidator.FrameLineValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultFrameInputHandler implements FrameInputHandler {

    @Autowired
    private FrameLineValidator frameLineValidator;

    @Autowired
    private FrameService frameService;

    @Override
    public void handle(final String frameInput) {
        frameLineValidator.validateFrameLine(frameInput);
        final String[] splitedLine = frameInput.split(" ");
        int pinfall = 0;
        if (!"F".equals(splitedLine[1])) {
            pinfall = Integer.parseInt(splitedLine[1]);
        }
        frameService.handleFrame(splitedLine[0], pinfall);
    }
}
