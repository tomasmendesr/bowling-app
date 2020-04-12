package jobcity.core.services.player;

import jobcity.core.entities.Frame;
import jobcity.core.entities.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {

    Player findByName(final String name);

    List<Player> findAll();

    Map<Player, List<Frame>> getFramesByPlayer();
}
