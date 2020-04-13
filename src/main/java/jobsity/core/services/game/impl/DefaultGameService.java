package jobsity.core.services.game.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;
import jobsity.core.services.frame.FrameService;
import jobsity.core.services.game.GameService;
import jobsity.core.services.player.PlayerService;
import jobsity.core.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static jobsity.core.services.frame.impl.DefaultFrameService.MAX_FRAME_NUMBER;

@Service
public class DefaultGameService implements GameService {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FrameService frameService;

    @Override
    public Boolean gameHasFinished() {
        final List<Player> players = playerService.findAll();
        if (CollectionUtils.isNotEmpty(players)) {
            final List<List<Frame>> playersFrames = getFramesFromPlayers(players);
            return playersFrames.stream().allMatch(this::didPlayerFinish);
        }
        return false;
    }

    @Override
    public List<List<Frame>> getFramesFromPlayers(final List<Player> players) {
        final List<List<Frame>> playersFrames = new ArrayList<>();
        players.forEach(player -> playersFrames.add(frameService.findByPlayer(player)));
        return playersFrames;
    }

    @Override
    public boolean didPlayerFinish(final List<Frame> playerFrames) {
        return playerFrames.size() >= 10 && playerFrames.size() <= MAX_FRAME_NUMBER;
    }

    @Override
    public void setFinalScores() {
        final List<Player> players = playerService.findAll();
    }
}
