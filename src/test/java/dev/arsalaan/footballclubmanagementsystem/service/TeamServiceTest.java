package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.LeagueDTO;
import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.TeamMapper;
import dev.arsalaan.footballclubmanagementsystem.model.League;
import dev.arsalaan.footballclubmanagementsystem.model.Team;
import dev.arsalaan.footballclubmanagementsystem.repository.LeagueRepository;
import dev.arsalaan.footballclubmanagementsystem.repository.TeamRepository;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// Requires @InjectMocks for Service (want to inject a mocked object into another mocked object).
// Service is dependent on Repository (and Mapper), so we can mock these classes using Mockito Annotation @Mock.

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private LeagueRepository leagueRepository;

    private Team team1;
    private Team team2;
    private TeamDTO teamDTO1;
    private TeamDTO teamDTO2;

    private List<Team> teams = new ArrayList<>();
    private List<TeamDTO> teamsDTO = new ArrayList<>();

    private League league1;
    private LeagueDTO leagueDTO1;
    private League league2;

    @BeforeEach
    public void setup(){
        team1 = Team.builder().teamId(1L).name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        team2 = Team.builder().teamId(2L).name("Manchester City").city("Manchester")
                .manager("Pep Guardiola").build();
        teams.add(team1);
        teams.add(team2);

        teamDTO1 = TeamDTO.builder().teamId(1L).name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        teamDTO2 = TeamDTO.builder().teamId(2L).name("Manchester City").city("Manchester")
                .manager("Pep Guardiola").build();
        teamsDTO.add(teamDTO1);
        teamsDTO.add(teamDTO2);

        league1 = League.builder().leagueId(1L).name("Premier League").country("England")
                .numberOfTeams(20).build();
        leagueDTO1 = LeagueDTO.builder().leagueId(1L).name("Premier League").country("England")
                .numberOfTeams(20).build();
        league2 = League.builder().leagueId(2L).name("La Liga").country("Spain")
                .numberOfTeams(20).build();
    }

    @Test
    public void givenTeamsList_whenGetAllTeams_thenReturnTeamsList() {

        // given
        given(teamRepository.findAll()).willReturn(teams);
        given(teamMapper.toTeamDTOs(teams)).willReturn(teamsDTO);

        // when
        List<TeamDTO> teamsList = teamService.getAllTeams();

        // then
        assertEquals(2, teamsList.size());
        assertThat(teamsList.get(1).getName()).isEqualTo(teams.get(1).getName());
        verify(teamRepository, times(1)).findAll();
    }

    @Test
    public void givenTeam_whenGetTeamById_thenReturnTeam() {

        // given team
        given(teamRepository.findById(1L)).willReturn(Optional.of(team1));
        given(teamMapper.toTeamDTO(team1)).willReturn(teamDTO1);

        // when
        TeamDTO savedTeam = teamService.getTeamById(1L);

        // then
        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getName()).isEqualTo(team1.getName());
    }

    @Test
    public void givenNonExistingTeamId_whenGetTeamById_thenThrowsException() {

        // given
        given(teamRepository.findById(1L)).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(ApiRequestException.class, () -> teamService.getTeamById(1L));

        // then
        assertThat(apiRequestException).hasMessageContaining("does not exist");
        verify(teamMapper, never()).toTeamDTO(any(Team.class));
    }

    @Test
    public void givenTeam_whenCreateTeam_thenSaveTeam(){

        // given - precondition or setup
        given(teamMapper.toTeam(teamDTO1)).willReturn(team1);
        given(teamRepository.findTeamByName(team1.getName())).willReturn(Optional.empty());
        given(teamRepository.save(team1)).willReturn(team1);

        // when - action or the behaviour that we are going test
        teamService.createTeam(teamDTO1);

        // then - verify the output
        verify(teamRepository, times(1)).save(team1);
    }

    @Test
    public void givenExistingTeam_whenCreateTeam_thenThrowsException(){

        // given - precondition or setup
        given(teamMapper.toTeam(teamDTO1)).willReturn(team1);
        given(teamRepository.findTeamByName(team1.getName())).willReturn(Optional.of(team1));

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(ApiRequestException.class,
                () -> teamService.createTeam(teamDTO1));

        // then
        assertThat(apiRequestException).hasMessageContaining("already exists");
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    public void givenTeam_whenUpdateTeamById_thenCallFindByIdAndTeamUpdated() {

        // given - precondition or setup
        given(teamRepository.findById(1L)).willReturn(Optional.of(team1));
        teamDTO1.setName("MUFC");
        teamDTO1.setCity("MCR");
        teamDTO1.setManager("EtH");

        // when - action or the behaviour that we are going test
        teamService.updateTeamById(teamDTO1.getTeamId(), teamDTO1);

        // then - verify the output
        assertThat(teamDTO1.getName()).isEqualTo("MUFC");
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    public void givenNonExistingTeamId_whenUpdateTeamById_thenThrowsException() {

        // given
        given(teamRepository.findById(50L)).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(ApiRequestException.class,
                () -> teamService.updateTeamById(50L, teamDTO1));

        // then
        assertThat(apiRequestException).hasMessageContaining("does not exist");
    }

    @Test
    public void givenTeamId_whenDeleteTeam_thenCallDeleteById() {

        // given - precondition or setup
        given(teamRepository.existsById(1L)).willReturn(true);
        willDoNothing().given(teamRepository).deleteById(1L);

        // when - action or the behaviour that we are going test
        teamService.deleteTeamById(1L);

        // then - verify the output
        verify(teamRepository, times(1)).deleteById(1L);
    }

    @Test
    public void givenNonExistingTeamId_whenDeleteTeam_thenThrowsException() {

        // given - precondition or setup
        given(teamRepository.existsById(50L)).willReturn(false);

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(ApiRequestException.class,
                () -> teamService.deleteTeamById(50L));

        // then
        assertThat(apiRequestException).hasMessageContaining("does not exist");
        verify(teamRepository, never()).deleteById(50L);
    }

    @Test
    public void givenLeagueWithTeams_whenViewAllTeamsForLeague_thenReturnTeamsList() {

        // given - precondition or setup
        league1.setTeams(teams);
        given(leagueRepository.findById(1L)).willReturn(Optional.of(league1));
        given(teamMapper.toTeamDTOs(league1.getTeams())).willReturn(teamsDTO);

        // when - action or the behaviour that we are going test
        List<TeamDTO> teamsList = teamService.viewAllTeamsForLeague(1L);

        // then
        assertEquals(2, teamsList.size());
        assertThat(teamsList.get(1).getName()).isEqualTo(teams.get(1).getName());
    }

    @Test
    public void givenNonExistingLeagueId_whenViewAllTeamsForLeague_thenThrowsException() {

        // given - precondition or setup
        given(leagueRepository.findById(50L)).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(
                ApiRequestException.class, () -> teamService.viewAllTeamsForLeague(50L));

        // then
        assertThat(apiRequestException).hasMessageContaining("does not exist");
        verify(teamMapper, never()).toTeamDTOs(teams);
    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeague_thenUpdateTeamUpdatedWithLeague() {

        // given - precondition or setup
        given(leagueRepository.findById(1L)).willReturn(Optional.of(league1));
        given(teamRepository.findById(1L)).willReturn(Optional.of(team1));

        // when - action or the behaviour that we are going test
        teamService.addTeamToLeague(1L,1L);

        // then
        assertThat(team1.getLeague().getName()).isEqualTo("Premier League");
        verify(leagueRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    public void givenNonExistingLeagueId_whenAddTeamToLeague_thenThrowsException() {

        // given - precondition or setup
        given(leagueRepository.findById(50L)).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(
                ApiRequestException.class, () -> teamService.addTeamToLeague(50L, 1L));

        // then
        assertThat(apiRequestException).hasMessageContaining("does not exist");
    }

    @Test
    public void givenNonExistingTeamId_whenAddTeamToLeague_thenThrowsException() {

        // given - precondition or setup
        given(leagueRepository.findById(1L)).willReturn(Optional.of(league1));
        given(teamRepository.findById(50L)).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(
                ApiRequestException.class, () -> teamService.addTeamToLeague(1L, 50L));

        // then
        assertThat(apiRequestException).hasMessageContaining("does not exist");
    }

    @Test
    public void givenLeagueWithTeam_whenAddTeamToLeague_thenThrowsException() {

        // given - precondition or setup
        team1.setLeague(league1);
        given(leagueRepository.findById(1L)).willReturn(Optional.of(league1));
        given(teamRepository.findById(1L)).willReturn(Optional.of(team1));

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(
                ApiRequestException.class, () -> teamService.addTeamToLeague(1L, 1L));

        // then
        assertThat(apiRequestException).hasMessageContaining("already assigned");
    }

    @Test
    public void givenLeagueWithTeam_whenRemoveTeamFromLeague_thenUpdatedTeamWithoutLeague() {

        // given - precondition or setup
        team1.setLeague(league1);
        given(leagueRepository.findById(1L)).willReturn(Optional.of(league1));
        given(teamRepository.findById(1L)).willReturn(Optional.of(team1));

        // when - action or the behaviour that we are going test
        teamService.removeTeamFromLeague(1L, 1L);

        // then - verify the output
        assertThat(team1.getLeague()).isNull();
    }

    @Test
    public void givenNonExistingLeagueId_whenRemoveTeamFromLeague_thenThrowsException() {

        // given - precondition or setup
        given(leagueRepository.findById(50L)).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(
                ApiRequestException.class, () -> teamService.removeTeamFromLeague(50L, 1L));

        // then
        assertThat(apiRequestException).hasMessageContaining("does not exist");
    }

    @Test
    public void givenNonExistingTeamId_whenRemoveTeamFromLeague_thenThrowsException() {

        // given - precondition or setup
        given(leagueRepository.findById(1L)).willReturn(Optional.of(league1));
        given(teamRepository.findById(50L)).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(
                ApiRequestException.class, () -> teamService.removeTeamFromLeague(1L, 50L));

        // then
        assertThat(apiRequestException).hasMessageContaining("does not exist");
    }

    @Test
    public void givenLeagueNotWithTeam_whenRemoveTeamFromLeague_thenThrowsException() {

        // given - precondition or setup
        team1.setLeague(league1);
        given(teamRepository.findById(1L)).willReturn(Optional.of(team1));
        given(leagueRepository.findById(2L)).willReturn(Optional.of(league2));

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(
                ApiRequestException.class, () -> teamService.removeTeamFromLeague(2L, 1L));

        // then
        assertThat(apiRequestException).hasMessageContaining("not assigned to League");
    }

    @Test
    public void givenTeamWithNoLeague_whenRemoveTeamFromLeague_thenThrowsException() {

        // given - precondition or setup
        given(leagueRepository.findById(1L)).willReturn(Optional.of(league1));
        given(teamRepository.findById(1L)).willReturn(Optional.of(team1));

        // when - action or the behaviour that we are going test
        ApiRequestException apiRequestException = Assertions.assertThrows(
                ApiRequestException.class, () -> teamService.removeTeamFromLeague(1L, 1L));

        // then
        assertThat(apiRequestException).hasMessageContaining("not assigned to any League");
    }

}
