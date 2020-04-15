package jobsity.core.services.game.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Pinfall;
import jobsity.core.entities.Player;
import jobsity.core.exceptions.BowlingApplicationException;
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
            frames.sort(Comparator.comparing(f -> f.getFrameNumber()));
            int i = 0;
            while (i < 10) {
                final Frame frame = frameService.findById(frames.get(i).getId());
                if (frame.getFrameNumber() >= 10) {
                    handleLastFrame(frames, 9);
                    i = 100;
                } else {
                    int framePinfallQuantity = pinfallService.calculateQuantityFromFrame(frame);
                    int previousFrameScore = i != 0 ? frameService.findById(frames.get(i - 1).getId()).getScore() : 0;
                    if (framePinfallQuantity == 10) {
                        boolean isAStrike = pinfallService.isAStrike(frame);
                        final Frame nextFrame = frames.get(i+1);
                        int nextFramePinfallQuantity = pinfallService.calculateQuantityFromFrame(nextFrame);
                        if (nextFramePinfallQuantity == 10) {
                            boolean nextFrameIsAStrike = pinfallService.isAStrike(nextFrame);
                            Frame nextNextFrame = frames.get(i+2);
                            int nextNextFramePinfallQuantity = pinfallService.calculateQuantityFromFrame(nextNextFrame);

                            if (nextNextFramePinfallQuantity == 10) {
                                boolean nextNextFrameIsAStrike = pinfallService.isAStrike(nextNextFrame);
                                int frameScore = isAStrike ? 30 : (10 + pinfallService.calculateQuantityFromFrame(nextFrame));
                                frame.setScore(previousFrameScore + frameScore);
                                if (nextNextFrameIsAStrike) {
                                    nextFrame.setScore(frame.getScore() + 30);
                                } else {
                                    int nextFrameScore = nextFrameIsAStrike ? 30 : (10 + pinfallService.calculateQuantityFromFrame(nextNextFrame));
                                    nextFrame.setScore(frame.getScore() + nextFrameScore);
                                }
                                int nextNextFrameScore = nextNextFrameIsAStrike ? 30 : 20;
                                nextNextFrame.setScore(nextFrame.getScore() + nextNextFrameScore);
                                frameService.save(frame);
                                frameService.save(nextFrame);
                                frameService.save(nextNextFrame);
                                i = nextNextFrame.getFrameNumber();
                            } else {
                                if (nextFrameIsAStrike) {
                                    frame.setScore(previousFrameScore + 10 + 10 + nextNextFramePinfallQuantity);
                                    nextFrame.setScore(frame.getScore() + 10 + nextFramePinfallQuantity);
                                    nextNextFrame.setScore(nextFrame.getScore() + nextNextFramePinfallQuantity);
                                } else {
                                    int frameScore = isAStrike ? (10 + nextFramePinfallQuantity) : (10 + pinfallService.getFirstPinfallQuantityFromFrame(nextFrame));
                                    frame.setScore(previousFrameScore + frameScore);
                                    nextFrame.setScore(frame.getScore() + 10 + pinfallService.getFirstPinfallQuantityFromFrame(nextNextFrame));
                                    nextNextFrame.setScore(nextFrame.getScore() + nextNextFramePinfallQuantity);
                                }
                                frameService.save(frame);
                                frameService.save(nextFrame);
                                frameService.save(nextNextFrame);
                                i = nextNextFrame.getFrameNumber();
                            }
                        } else {
                            // hizo strike y en el proximo no tiró 10
                            if (isAStrike) {
                                frame.setScore(previousFrameScore + 10 + nextFramePinfallQuantity);
                            } else {
                                frame.setScore(previousFrameScore + 10 + pinfallService.getFirstPinfallQuantityFromFrame(nextFrame));
                            }
                            nextFrame.setScore(frame.getScore() + nextFramePinfallQuantity);
                            frameService.save(frame);
                            frameService.save(nextFrame);
                            i = nextFrame.getFrameNumber();
                        }
                    } else {
                        frame.setScore(previousFrameScore + framePinfallQuantity);
                        frameService.save(frame);
                        i++;
                    }
                }
            }
        }
    }

    private void handleLastFrame(List<Frame> frames, int i) {
        try {
            Frame frame = frames.get(i);
            Frame previousFrame = frameService.findById(frames.get(i - 1).getId());
            int quantity = pinfallService.calculateQuantityFromFrame(frame);
            if (quantity == 10) {
                Frame nextFrame = frames.get(i + 1);
                int quantityNext = pinfallService.calculateQuantityFromFrame(nextFrame);
                if (quantityNext == 10) {
                    Frame nextNextFrame = frames.get(i + 2);
                    frame.setScore(previousFrame.getScore() + 20 + pinfallService.calculateQuantityFromFrame(nextNextFrame));
                } else {
                    frame.setScore(previousFrame.getScore() + 10 + quantityNext);
                }
            } else {
                frame.setScore(previousFrame.getScore() + quantity);
            }
            frameService.save(frame);
        } catch (Exception e) {
            throw new BowlingApplicationException("The file is incomplete.");
        }
    }

}
