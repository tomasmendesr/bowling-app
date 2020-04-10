package jobcity.core.services.frame.impl;

import jobcity.core.entities.Frame;
import jobcity.core.entities.Pinfall;
import jobcity.core.entities.Player;
import jobcity.core.exceptions.BowlingApplicationException;
import jobcity.core.exceptions.FrameException;
import jobcity.core.repositories.FrameRepository;
import jobcity.core.services.frame.FrameService;
import jobcity.core.services.pinfall.PinfallService;
import jobcity.core.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class DefaultFrameService implements FrameService {

    private static final Integer FIRST_FRAME_NUMBER = 1;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PinfallService pinfallService;

    @Autowired
    private FrameRepository frameRepository;

    @Override
    public void handleFrame(final String playerName, final int pinfalls) {
        final Player player = playerService.findByName(playerName);
        final List<Frame> playerFrames = frameRepository.findByPlayer(player);
        final Frame lastFrame = getLastFrame(playerFrames, player);
        Frame currentFrame = lastFrame;
        if (isComplete(lastFrame)) {
            final int currentFrameNumber = lastFrame.getFrameNumber() + 1;
            if (currentFrameNumber > 10) {
                throw new BowlingApplicationException("The player '" + playerName + "' has more than 10 frames");
            }
            currentFrame = createFrame(player, currentFrameNumber);
        }
        pinfallService.save(currentFrame, pinfalls);
    }

    private Frame getLastFrame(final List<Frame> frames, final Player player) {
        if (Objects.isNull(frames) || frames.isEmpty()) {
            return createFrame(player, FIRST_FRAME_NUMBER);
        } else {
           return getLastFrame(frames);
        }
    }

    private Frame getLastFrame(List<Frame> frames) {
        return Collections.max(frames, Comparator.comparing(f -> f.getFrameNumber()));
    }

    private Frame createFrame(final Player player, final int frameNumber) {
        Frame frame = new Frame();
        frame.setPlayer(player);
        frame.setFrameNumber(frameNumber);
        return frame;
    }

    private Boolean isComplete(final Frame frame) {
        final List<Pinfall> pinfalls = pinfallService.findByFrame(frame);
        if (Objects.isNull(pinfalls) || pinfalls.isEmpty()) {
            return false;
        }

        if (pinfalls.get(0).getQuantity() == 10) {
            return true;
        }

        return pinfalls.size() == 2;
    }
}
