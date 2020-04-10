package jobcity.core.services.player;

import jobcity.core.entities.Player;

public interface PlayerService {

    Player findByName(final String name);
}
