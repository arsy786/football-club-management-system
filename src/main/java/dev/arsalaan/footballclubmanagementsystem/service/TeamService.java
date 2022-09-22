package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.TeamMapper;
import dev.arsalaan.footballclubmanagementsystem.model.League;
import dev.arsalaan.footballclubmanagementsystem.model.Team;
import dev.arsalaan.footballclubmanagementsystem.repository.LeagueRepository;
import dev.arsalaan.footballclubmanagementsystem.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, LeagueRepository leagueRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.leagueRepository = leagueRepository;
        this.teamMapper = teamMapper;
    }

    // [GET] View All Teams
    public List<TeamDTO> getAllTeams() {
        List<Team> teams = teamRepository.findAll();

        log.info("From DB, Got Teams");
        return teamMapper.toTeamDTOs(teams);
    }

    // [GET] View a specific Team by its ID
    public TeamDTO getTeamById(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("team with id " + teamId + " does not exist"));

        log.info("From DB, Got Team with id: {}", teamId);
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
        log.info("In DB, Created new Team with generated id: {}", team.getTeamId());
    }

    // [PUT] Update a specific Team by its ID
    @Transactional
    public void updateTeamById(Long teamId, TeamDTO teamDTO) {

        // retrieve team entity by id
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("team with id " + teamId + " does not exist"));

        // no checks as @Valid in Controller used
        team.setName(teamDTO.getName());
        team.setCity(teamDTO.getCity());
        team.setManager(teamDTO.getManager());

        // Team updatedTeam = teamRepository.save(team); - would need this if @Transactional not used

        log.info("From DB, Updated Team with id: {}", teamId);

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
        log.info("From DB, Deleted Team with id: {}", teamId);
    }

    // [GET] View All Teams for League ID
    public List<TeamDTO> viewAllTeamsForLeague(Long leagueId) {

        League league = leagueRepository.findById(leagueId).orElseThrow(
                () -> new ApiRequestException("League with id " + leagueId + " does not exist"));

        log.info("From DB, Retrieved Teams with League id: {}", leagueId);
        return teamMapper.toTeamDTOs(league.getTeams());
    }

    // [PUT] Add a Team to a specific League
    @Transactional
    public void addTeamToLeague(Long leagueId, Long teamId) {

        League league = leagueRepository.findById(leagueId).orElseThrow(
                () -> new ApiRequestException("League with id " + leagueId + " does not exist"));
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("Team with id " + teamId + " does not exist"));

        if (Objects.nonNull(team.getLeague())) {
            throw new ApiRequestException("Team with id " + teamId + " already assigned to League with id " + team.getLeague().getLeagueId());
        }

        team.setLeague(league);
        log.info("From DB, Added Team with id: {} to League with id: {}", teamId, leagueId);
    }

    // [DELETE] Remove a specific Team from a League
    @Transactional
    public void removeTeamFromLeague(Long leagueId, Long teamId) {

        League league = leagueRepository.findById(leagueId).orElseThrow(
                () -> new ApiRequestException("League with id " + leagueId + " does not exist"));
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("Team with id " + teamId + " does not exist"));

        if (Objects.isNull(team.getLeague())) {
            throw new ApiRequestException("Team with id " + teamId + " is not assigned to any League");
        }

        if(!team.getLeague().getLeagueId().equals(league.getLeagueId())) {
            throw new ApiRequestException("Team with id " + teamId + " is not assigned to League with id " + leagueId);
        }

        team.setLeague(null); // sets league field in team to null instead of removing the parents AND deleting child
        log.info("From DB, Removed Team with id: {} from League with id: {}", teamId, leagueId);
    }

}
