package jobcity.core.services.player.impl;

import jobcity.core.entities.Player;
import jobcity.core.repositories.PlayerRepository;
import jobcity.core.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DefaultPlayerService implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player findByName(final String name) {
         Player player = playerRepository.findByName(name);
         if (Objects.isNull(player)) {
             player = new Player();
             player.setName(name);
             player = playerRepository.save(player);
         }
         return player;
    }
}
