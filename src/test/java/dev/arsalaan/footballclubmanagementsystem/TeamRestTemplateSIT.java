package dev.arsalaan.footballclubmanagementsystem;

import dev.arsalaan.footballclubmanagementsystem.dto.LeagueDTO;
import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeamRestTemplateSIT {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void givenTeams_whenGetAllTeams_thenStatusOkAndBodyCorrect() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();

        TeamDTO teamDTO2 = TeamDTO.builder().name("Manchester City").city("Manchester")
                .manager("Pep Guardiola").build();

        TeamDTO teamDTO3 = TeamDTO.builder().name("Real Madrid").city("Madrid")
                .manager("Carlo Ancelotti").build();

        String path = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path, teamDTO1, HttpHeaders.class);
        restTemplate.postForEntity(path, teamDTO2, HttpHeaders.class);
        restTemplate.postForEntity(path, teamDTO3, HttpHeaders.class);

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("Manchester United", "Manchester City", "Real Madrid");

    }

    @Test
    public void givenNothing_whenGetAllTeams_thenStatusNoContent() {

        // given

        // when
        String path = "http://localhost:"+randomServerPort+"/api/v1/team/";
        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    public void givenTeam_whenGetTeamById_thenStatusOkAndBodyCorrect() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, HttpHeaders.class);

        // when
        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        ResponseEntity<String> response = restTemplate.getForEntity(path2, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("Manchester United");

    }


// do not think this is possible given Java Bean validation in DTO class now!
    //    @Test
//    public void givenEmptyTeam_whenGetTeamById_thenStatusNotFound() {
//
//        // given
//        TeamDTO teamDTO = null;
//        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
//        ResponseEntity<HttpHeaders> response1 = restTemplate.postForEntity(path1, null, HttpHeaders.class);
//
//        String path = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
//
//        // when
//        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class, "1");
//
//        // then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//
//    }

    @Test
    public void givenNothing_whenGetTeamById_thenStatusBadRequest() {

        // given

        // when
        String path = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }


    @Test
    public void givenTeam_whenCreateTeam_thenStatusCreated() {

        // given
        TeamDTO teamDTO = TeamDTO.builder().name("Manchester United").city("Manchester").manager("Erik ten Hag").build();

        // when
        String path = "http://localhost:"+randomServerPort+"/api/v1/team/";
        ResponseEntity<HttpHeaders> response = restTemplate.postForEntity(path, teamDTO, HttpHeaders.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    public void givenTeamWithSameName_whenCreateTeam_thenStatusBadRequest() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester").manager("Erik ten Hag").build();
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        // when
        TeamDTO teamDTO2 = TeamDTO.builder().name("Manchester United").city("Mcr").manager("EtH").build();
        ResponseEntity<ApiRequestException> response = restTemplate.postForEntity(path1, teamDTO2, ApiRequestException.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void givenTeam_whenUpdateTeamById_thenStatusOk() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester").manager("Erik ten Hag").build();
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        // when
        TeamDTO teamDTO2 = TeamDTO.builder().name("MUFC").city("MCR").manager("EtH").build();
        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        HttpEntity<TeamDTO> requestEntity = new HttpEntity<>(teamDTO2);

        ResponseEntity<String> response = restTemplate
                .exchange(path2, HttpMethod.PUT, requestEntity, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void givenTeam_whenUpdateTeamByIdWithWrongId_thenStatusBadRequest() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester").manager("Erik ten Hag").build();
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        // when
        TeamDTO teamDTO2 = TeamDTO.builder().name("MUFC").city("MCR").manager("EtH").build();
        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        HttpEntity<TeamDTO> requestEntity = new HttpEntity<>(teamDTO2);

        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(path2, HttpMethod.PUT, requestEntity, ApiRequestException.class, "2");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void givenTeam_whenDeleteTeamById_thenStatusNoContent() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        // when
        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        HttpEntity<?> requestEntity = HttpEntity.EMPTY;

        ResponseEntity<String> response = restTemplate
                .exchange(path2, HttpMethod.DELETE, requestEntity, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    public void givenTeam_whenDeleteTeamByIdWithWrongId_thenStatusBadRequest() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        // when
        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        HttpEntity<?> requestEntity = HttpEntity.EMPTY;
        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(path2, HttpMethod.DELETE, requestEntity, ApiRequestException.class, "2");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void givenLeagueWithTeams_whenViewAllTeamsForLeague_thenStatusOkAndBodyCorrect() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        TeamDTO teamDTO2 = TeamDTO.builder().name("Manchester City").city("Manchester")
                .manager("Pep Guardiola").build();
        LeagueDTO leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();

        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);
        restTemplate.postForEntity(pathCreateTeam, teamDTO2, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, HttpHeaders.class, "1", "1");
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, HttpHeaders.class, "2", "1");

        // when
        String pathViewAllTeamsForLeague = "http://localhost:"+randomServerPort+"/api/v1/team/league/{leagueId}";
        ResponseEntity<String> response = restTemplate.getForEntity(pathViewAllTeamsForLeague, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("Manchester United", "Manchester City");

    }

    @Test
    public void givenLeagueWithNoTeams_whenViewAllTeamsForLeague_thenStatusNoContent() {

        // given
        LeagueDTO leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        // when
        String pathViewAllTeamsForLeague = "http://localhost:"+randomServerPort+"/api/v1/team/league/{leagueId}";
        ResponseEntity<String> response = restTemplate.getForEntity(pathViewAllTeamsForLeague, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    public void givenNothing_whenViewAllTeamsForLeague_thenStatusBadRequest() {

        // given

        // when
        String pathViewAllTeamsForLeague = "http://localhost:"+randomServerPort+"/api/v1/team/league/{leagueId}";
        ResponseEntity<String> response = restTemplate.getForEntity(pathViewAllTeamsForLeague, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }


    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeague_thenStatusCreated() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        LeagueDTO leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();
        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        // when
        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        ResponseEntity<String> response = restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeagueWithWrongLeagueId_thenStatusBadRequest() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();

        LeagueDTO leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();

        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        // when
        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        ResponseEntity<String> response = restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "50");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeagueWithWrongTeamId_thenStatusBadRequest() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();

        LeagueDTO leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();

        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        // when
        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        ResponseEntity<String> response = restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "50", "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeague_thenStatusNoContent() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        LeagueDTO leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();
        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "1");

        // when
        String pathRemoveTeamFromLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(pathRemoveTeamFromLeague, HttpMethod.DELETE, HttpEntity.EMPTY, ApiRequestException.class, "1", "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeagueWithWrongLeagueId_thenStatusBadRequest() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        LeagueDTO leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();
        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "1");

        // when
        String pathRemoveTeamFromLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(pathRemoveTeamFromLeague, HttpMethod.DELETE, HttpEntity.EMPTY, ApiRequestException.class, "1", "50");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeagueWithWrongTeamId_thenStatusBadRequest() {

        // given
        TeamDTO teamDTO1 = TeamDTO.builder().name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        LeagueDTO leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();
        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "1");

        // when
        String pathRemoveTeamFromLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(pathRemoveTeamFromLeague, HttpMethod.DELETE, HttpEntity.EMPTY, ApiRequestException.class, "50", "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

}
