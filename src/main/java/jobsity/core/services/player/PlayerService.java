package jobsity.core.services.player;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {

    /**
     * Returns a player searching by name
     * @param name
     * @return
     */
    Player findByName(final String name);

    /**
     * Returns all the players
     * @return
     */
    List<Player> findAll();

    /***
     * Returns a map of frames by player
     * @return
     */
    Map<Player, List<Frame>> getFramesByPlayer();
}
