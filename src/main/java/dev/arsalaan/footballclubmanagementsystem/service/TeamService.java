package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.TeamMapper;
import dev.arsalaan.footballclubmanagementsystem.model.Team;
import dev.arsalaan.footballclubmanagementsystem.repository.TeamRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    // [GET] View All Teams
    public List<TeamDTO> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teamMapper.toTeamDTOs(teams);
    }

    // [GET] View a specific Team by its ID
    public TeamDTO getTeamById(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("team with id " + teamId + " does not exist"));

        return teamMapper.toTeamDTO(team);
    }

    // [POST] Create a Team
    public void createTeam(TeamDTO teamDTO) {

        // convert DTO to entity
        Team team = teamMapper.toTeam(teamDTO);

        // check if Team name exists (NOTE: repository layer can only consume/return entities)
        Optional<Team> teamOptional = teamRepository.findTeamByName(team.getName());

        if (teamOptional.isPresent()) {
            throw new ApiRequestException("team name " + team.getName() + " already exists");
        }

        // convert entity to DTO
        // TeamDTO newTeamDTO = teamRepository.save(team); - would need this if response to method call was TeamDTO

        teamRepository.save(team);
    }

    // [PUT] Update a specific Team by its ID
    @Transactional
    public void updateTeamById(Long teamId, TeamDTO teamDTO) {

        // retrieve team entity by id
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("team with id " + teamId + " does not exist"));

        // checks
        if (teamDTO.getName() != null && teamDTO.getName().length() > 0 && !Objects.equals(team.getName(), teamDTO.getName())) {
            team.setName(teamDTO.getName());
        }

        if (teamDTO.getCity() != null && teamDTO.getCity().length() > 0 && !Objects.equals(team.getCity(), teamDTO.getCity())) {
            team.setCity(teamDTO.getCity());
        }

        if (teamDTO.getManager() != null && teamDTO.getManager().length() > 0 && !Objects.equals(team.getManager(), teamDTO.getManager())) {
            team.setManager(teamDTO.getManager());
        }

        // Team updatedTeam = teamRepository.save(team); - would need this if @Transactional not used
        // return teamMapper.toTeamDTO(updatedTeam); - would need this if response DTO was required

    }

    // [DELETE] Remove a specific Team by its ID
    public void deleteTeamById(Long teamId) {

        // check
        boolean exists = teamRepository.existsById(teamId);

        if (!exists) {
            throw new ApiRequestException("team with id " + teamId + " does not exist");
        }

        teamRepository.deleteById(teamId);
    }


}
