package jobsity.core.services.player;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {

    Player findByName(final String name);

    List<Player> findAll();

    Map<Player, List<Frame>> getFramesByPlayer();
}
