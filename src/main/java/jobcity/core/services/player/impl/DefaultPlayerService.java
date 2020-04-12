package jobcity.core.services.player.impl;

import jobcity.core.entities.Frame;
import jobcity.core.entities.Player;
import jobcity.core.repositories.PlayerRepository;
import jobcity.core.services.frame.FrameService;
import jobcity.core.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultPlayerService implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private FrameService frameService;

    @Override
    public Player findByName(final String name) {
         Player player = playerRepository.findByName(name);
         if (Objects.isNull(player) || Objects.isNull(player.getId())) {
             player = new Player();
             player.setName(name);
             player = playerRepository.save(player);
         }
         return player;
    }

    @Override
    public List<Player> findAll() {
        List players = playerRepository.findAll();
        return Objects.isNull(players) ? new ArrayList<>() : players;
    }

    @Override
    public Map<Player, List<Frame>> getFramesByPlayer() {
        Map<Player, List<Frame>> result = new HashMap<>();
        findAll().forEach(player -> result.put(player, frameService.findByPlayer(player)));
        return result;
    }
}
