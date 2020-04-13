package jobsity.core.services.game.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;
import jobsity.core.services.frame.FrameService;
import jobsity.core.services.game.GameService;
import jobsity.core.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static jobsity.core.services.frame.impl.DefaultFrameService.MAX_FRAME_NUMBER;

@Service
public class DefaultGameService implements GameService {

    private PlayerService playerService;

    @Autowired
    public DefaultGameService (PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public Boolean gameHasFinished() {
        Map<Player, List<Frame>> framesByPlayer = playerService.getFramesByPlayer();
        AtomicBoolean gameHasFinished = new AtomicBoolean(true);
        for (Map.Entry<Player, List<Frame>> entry : framesByPlayer.entrySet()) {
            List<Frame> frames = entry.getValue();
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
        // TODO
    }
}
