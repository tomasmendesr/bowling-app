package jobsity.core.services.game.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;
import jobsity.core.services.frame.FrameService;
import jobsity.core.services.game.GameService;
import jobsity.core.services.pinfall.PinfallService;
import jobsity.core.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static jobsity.core.services.frame.impl.DefaultFrameService.MAX_FRAME_NUMBER;

@Service
public class DefaultGameService implements GameService {

    public static final int LAST_VALID_FRAME_NUMBER = 10;

    private PlayerService playerService;
    private PinfallService pinfallService;
    private FrameService frameService;

    @Autowired
    public DefaultGameService (PlayerService playerService, PinfallService pinfallService, FrameService frameService) {
        this.playerService = playerService;
        this.pinfallService = pinfallService;
        this.frameService = frameService;
    }

    @Override
    public Boolean gameHasFinished() {
        final Map<Player, List<Frame>> framesByPlayer = playerService.getFramesByPlayer();
        final AtomicBoolean gameHasFinished = new AtomicBoolean(true);
        for (Map.Entry<Player, List<Frame>> entry : framesByPlayer.entrySet()) {
            final List<Frame> frames = entry.getValue();
            final boolean areFramesComplete = areFramesComplete(frames);
            if (!areFramesComplete) {
                gameHasFinished.set(false);
                break;
            }
        }
        return gameHasFinished.get();
    }

    @Override
    public boolean areFramesComplete(final List<Frame> playerFrames) {
        return playerFrames.size() >= 10 && playerFrames.size() <= MAX_FRAME_NUMBER;
    }

    @Override
    public void setFinalScores() {
        final Map<Player, List<Frame>> framesByPlayer = playerService.getFramesByPlayer();
        for (Map.Entry<Player, List<Frame>> entry : framesByPlayer.entrySet()) {
            List<Frame> frames = entry.getValue();
            frames.sort(Comparator.comparing(Frame::getFrameNumber));
            calculateScoreForFrames(frames);
        }
    }

    private void calculateScoreForFrames(List<Frame> frames) {
        int i = 0;
        while (i < LAST_VALID_FRAME_NUMBER) {
            final Frame frame = frameService.findById(frames.get(i).getId());
            int previousFrameScore = i != 0 ? frameService.findById(frames.get(i - 1).getId()).getScore() : 0;
            if (frame.getFrameNumber() == MAX_FRAME_NUMBER) {
                frame.setScore(previousFrameScore + pinfallService.calculateQuantityFromFrame(frames.get(i)));
                frameService.save(frame);
                break;
            }
            int framePinfallQuantity = pinfallService.calculateQuantityFromFrame(frame);
            if (pinfallService.isAStrikeOrSpear(framePinfallQuantity)) {
                boolean isAStrike = pinfallService.isAStrike(frame);
                final Frame nextFrame = frames.get(i+1);
                if (isAStrike) {
                    boolean nextFrameIsAStrike = pinfallService.isAStrike(nextFrame);
                    if (nextFrameIsAStrike) {
                            final Frame nextNextFrame = frames.get(i+2);
                            boolean nextNextFrameIsAStrike = pinfallService.isAStrike(nextNextFrame);
                            if (nextNextFrameIsAStrike) {
                                saveScoreForFrame(frame, previousFrameScore + 30);
                                if (frame.getFrameNumber() == MAX_FRAME_NUMBER - 2) {
                                    saveScoreForFrame(nextFrame, frame.getScore() + 10 + pinfallService.calculateQuantityFromFrame(nextNextFrame));
                                    saveScoreForFrame(nextNextFrame, nextFrame.getScore() + pinfallService.calculateQuantityFromFrame(nextNextFrame, false));
                                    break;
                                }
                                i++;
                            } else {
                                saveScoreForFrame(frame, previousFrameScore + 20 + pinfallService.getFirstPinfallQuantityFromFrame(nextNextFrame));
                                saveScoreForFrame(nextFrame, frame.getScore() + 10 + pinfallService.calculateQuantityFromFrame(nextNextFrame));
                                i = nextFrame.getFrameNumber();
                            }
                    } else {
                        saveScoreForFrame(frame, previousFrameScore + 10 + pinfallService.calculateQuantityFromFrame(nextFrame));
                        i++;
                    }
                } else {
                    saveScoreForFrame(frame, previousFrameScore + 10 + pinfallService.getFirstPinfallQuantityFromFrame(nextFrame));
                    i++;
                }
            } else {
                saveScoreForFrame(frame, previousFrameScore + framePinfallQuantity);
                i++;
            }
        }
    }

    private void saveScoreForFrame(Frame frame, int score) {
        frame.setScore(score);
        frameService.save(frame);
    }
}
