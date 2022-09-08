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
    public void updatePlayerById(Long playerId, PlayerDTO playerDTO) {

        Player player = playerRepository.findById(playerId).orElseThrow(
                () -> new ApiRequestException("player with id " + playerId + " does not exist"));

        if (playerDTO.getName() != null && playerDTO.getName().length() > 0 && !Objects.equals(player.getName(), playerDTO.getName())) {
            player.setName(playerDTO.getName());
        }

        if (playerDTO.getPosition() != null && playerDTO.getPosition().length() > 0 && !Objects.equals(player.getPosition(), playerDTO.getPosition())) {
            player.setPosition(playerDTO.getPosition());
        }

        if (playerDTO.getNationality() != null && playerDTO.getNationality().length() > 0 && !Objects.equals(player.getNationality(), playerDTO.getNationality())) {
            player.setNationality(playerDTO.getNationality());
        }

        if (playerDTO.getAge() != null && playerDTO.getAge() > 15 && !Objects.equals(player.getAge(), playerDTO.getAge())) {
            player.setAge(playerDTO.getAge());
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
