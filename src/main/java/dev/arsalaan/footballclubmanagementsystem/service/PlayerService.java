package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.PlayerDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.PlayerMapper;
import dev.arsalaan.footballclubmanagementsystem.model.Team;
import dev.arsalaan.footballclubmanagementsystem.model.Player;
import dev.arsalaan.footballclubmanagementsystem.repository.PlayerRepository;
import dev.arsalaan.footballclubmanagementsystem.repository.TeamRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
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

    public List<PlayerDTO> viewAllPlayersForTeam(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("Team with id " + teamId + " does not exist"));

        return playerMapper.toPlayerDTOs(team.getPlayers());
    }

    @Transactional
    public void addPlayerToTeam(Long teamId, Long playerId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("Team with id " + teamId + " does not exist"));
        Player player = playerRepository.findById(playerId).orElseThrow(
                () -> new ApiRequestException("Player with id " + playerId + " does not exist"));

        if (Objects.nonNull(player.getTeam())) {
            throw new ApiRequestException("Player with id " + playerId + " already assigned to Team with id " + player.getTeam().getTeamId());
        }

        player.setTeam(team);
    }

    @Transactional
    public void removePlayerFromTeam(Long teamId, Long playerId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("Team with id " + teamId + " not found"));
        Player player = playerRepository.findById(playerId).orElseThrow(
                () -> new ApiRequestException("Player with id " + playerId + " does not exist"));

        if(!player.getTeam().getTeamId().equals(team.getTeamId())) {
            throw new ApiRequestException("Player with id " + playerId + " is not assigned to Team with id " + teamId);
        }

        if (Objects.isNull(player.getTeam())) {
            throw new ApiRequestException("Player with id " + playerId + " is not assigned to any Team");
        }

        player.setTeam(null); // sets team field in player to null instead of removing the parents AND deleting child
    }
    
}
