package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.PlayerDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.PlayerMapper;
import dev.arsalaan.footballclubmanagementsystem.model.Player;
import dev.arsalaan.footballclubmanagementsystem.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = playerRepository.findAll();
        return playerMapper.toPlayerDTOs(players);
    }

    public PlayerDTO getPlayerById(Long playerId) {

        Player player = playerRepository.findById(playerId).orElseThrow(
                () -> new ApiRequestException("player with id " + playerId + " does not exist"));

        return playerMapper.toPlayerDTO(player);
    }

    public void createPlayer(PlayerDTO playerDTO) {

        Player player = playerMapper.toPlayer(playerDTO);

        // no player name check as two players can have the same name (a name is not unique)

        playerRepository.save(player);
    }

    @Transactional
    public void updatePlayerById(Long playerId, String name, String position, String nationality, Integer age) {

        Player player = playerRepository.findById(playerId).orElseThrow(
                () -> new ApiRequestException("player with id " + playerId + " does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(player.getName(), name)) {
            player.setName(name);
        }

        if (position != null && position.length() > 0 && !Objects.equals(player.getPosition(), position)) {
            player.setPosition(position);
        }

        if (nationality != null && nationality.length() > 0 && !Objects.equals(player.getNationality(), nationality)) {
            player.setNationality(nationality);
        }

        if (age != null && age > 15 && !Objects.equals(player.getAge(), age)) {
            player.setAge(age);
        }

    }

    public void deletePlayerById(Long playerId) {

        boolean exists = playerRepository.existsById(playerId);

        if (!exists) {
            throw new ApiRequestException("player with id " + playerId + " does not exist");
        }

        playerRepository.deleteById(playerId);
    }
    
}
