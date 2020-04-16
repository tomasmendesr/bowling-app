package jobsity.core.services.frame.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Pinfall;
import jobsity.core.entities.Player;
import jobsity.core.exceptions.BowlingApplicationException;
import jobsity.core.repositories.FrameRepository;
import jobsity.core.services.frame.FrameService;
import jobsity.core.services.pinfall.PinfallService;
import jobsity.core.services.player.PlayerService;
import jobsity.core.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DefaultFrameService implements FrameService {

    private static final Integer FIRST_FRAME_NUMBER = 1;
    public static final Integer MAX_FRAME_NUMBER = 10;

    private PlayerService playerService;
    private PinfallService pinfallService;
    private FrameRepository frameRepository;

    @Autowired
    public DefaultFrameService(PlayerService playerService, PinfallService pinfallService, FrameRepository frameRepository) {
        this.playerService = playerService;
        this.pinfallService = pinfallService;
        this.frameRepository = frameRepository;
    }

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
                throw new BowlingApplicationException("The player '" + lastFrame.getPlayer().getName() + "' has more than " + MAX_FRAME_NUMBER + " frames");
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
        return save(frame);
    }

    @Override
    public Boolean isFrameComplete(final Frame frame) {
        final List<Pinfall> pinfalls = pinfallService.findByFrame(frame);
        if (CollectionUtils.isEmpty(pinfalls)) {
            return false;
        }

        if (isTheLastFrame(frame)) {
            if (pinfalls.size() == 3) throw new BowlingApplicationException("The input data is invalid.");
            return pinfallService.calculateQuantityFromFrame(frame) < 10;
        }

        if (pinfalls.get(0).getQuantity() == 10) {
            return true;
        }

        return pinfalls.size() == 2;
    }

    public boolean isTheLastFrame(final Frame frame) {
        return frame.getFrameNumber() == MAX_FRAME_NUMBER;
    }

    @Override
    public List<Frame> findByPlayer(Player player) {
        final List<Frame> frames = frameRepository.findByPlayer(player);
        frames.sort(Comparator.comparing(f -> f.getFrameNumber()));
        return frames;
    }

    @Override
    public Frame save(Frame frame) {
       return frameRepository.save(frame);
    }

    @Override
    public Frame findById(Long id) {
        return frameRepository.findById(id).get();
    }
}
