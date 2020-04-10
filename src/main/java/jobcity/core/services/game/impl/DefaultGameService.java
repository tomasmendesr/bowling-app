package jobcity.core.services.game.impl;

import jobcity.core.entities.Player;
import jobcity.core.repositories.PlayerRepository;
import jobcity.core.services.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DefaultGameService implements GameService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Boolean gameHasFinished() {
        final List<Player> players = playerRepository.findAll();
        return Objects.nonNull(players) && !players.isEmpty() && players.stream().allMatch(player -> Objects.nonNull(player.getScore()));
    }
}
