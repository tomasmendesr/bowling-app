package jobcity.core.services.frame.impl;

import jobcity.core.entities.Frame;
import jobcity.core.entities.Pinfall;
import jobcity.core.entities.Player;
import jobcity.core.exceptions.BowlingApplicationException;
import jobcity.core.repositories.FrameRepository;
import jobcity.core.services.frame.FrameService;
import jobcity.core.services.pinfall.PinfallService;
import jobcity.core.services.player.PlayerService;
import jobcity.core.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DefaultFrameService implements FrameService {

    private static final Integer FIRST_FRAME_NUMBER = 1;
    public static final Integer MAX_FRAME_NUMBER = 12;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PinfallService pinfallService;

    @Autowired
    private FrameRepository frameRepository;

    @Override
    public void handleFrame(final String playerName, final int pinfalls) {
        final Player player = playerService.findByName(playerName);
        final List<Frame> playerFrames = findByPlayer(player);
        final Frame lastFrame = CollectionUtils.isEmpty(playerFrames) ? saveFrame(player, FIRST_FRAME_NUMBER) : getLastFrame(playerFrames);
        final Frame currentFrame = getCurrentFrame(lastFrame);
        pinfallService.saveNewPinfall(currentFrame, pinfalls);
    }

    @Override
    public Frame getCurrentFrame(final Frame lastFrame) {
        if (isFrameComplete(lastFrame)) {
            final int currentFrameNumber = lastFrame.getFrameNumber() + 1;
            if (currentFrameNumber > MAX_FRAME_NUMBER) {
                throw new BowlingApplicationException("The player '" + lastFrame.getPlayer() + "' has more than 12 frames");
            }
            return saveFrame(lastFrame.getPlayer(), currentFrameNumber);
        }

        return lastFrame;
    }

    @Override
    public Frame getLastFrame(final List<Frame> frames) {
        return Collections.max(frames, Comparator.comparing(f -> f.getFrameNumber()));
    }

    @Override
    public Frame saveFrame(final Player player, final int frameNumber) {
        Frame frame = new Frame();
        frame.setPlayer(player);
        frame.setFrameNumber(frameNumber);
        return frameRepository.save(frame);
    }

    @Override
    public Boolean isFrameComplete(final Frame frame) {
        final List<Pinfall> pinfalls = pinfallService.findByFrame(frame);
        if (CollectionUtils.isEmpty(pinfalls)) {
            return false;
        }

        if (pinfalls.get(0).getQuantity() == 10) {
            return true;
        }

        return pinfalls.size() == 2;
    }

    @Override
    public List<Frame> findByPlayer(Player player) {
        final List<Frame> frames = frameRepository.findByPlayer(player);
        frames.sort(Comparator.comparing(f -> f.getFrameNumber()));
        return frames;
    }
}
