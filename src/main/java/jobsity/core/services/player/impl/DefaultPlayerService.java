package jobsity.core.services.player.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;
import jobsity.core.repositories.FrameRepository;
import jobsity.core.repositories.PlayerRepository;
import jobsity.core.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultPlayerService implements PlayerService {

    private PlayerRepository playerRepository;
    private FrameRepository frameRepository;

    @Autowired
    public DefaultPlayerService(PlayerRepository playerRepository, FrameRepository frameRepository){
        this.playerRepository = playerRepository;
        this.frameRepository = frameRepository;
    }

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
        return playerRepository.findAll();
    }

    @Override
    public Map<Player, List<Frame>> getFramesByPlayer() {
        Map<Player, List<Frame>> result = new HashMap<>();
        findAll().forEach(player -> result.put(player, frameRepository.findByPlayer(player)));
        return result;
    }
}
