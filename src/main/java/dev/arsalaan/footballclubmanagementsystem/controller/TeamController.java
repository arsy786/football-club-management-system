package dev.arsalaan.footballclubmanagementsystem.controller;

import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // [GET] View All Teams
    @Operation(summary = "This is to view all the Teams")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched All the Teams from Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "No teams in Db", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {

        log.info("Received GET /api/v1/team/ request.");

        List<TeamDTO> teamsDTO = teamService.getAllTeams();

        if (teamsDTO == null || teamsDTO.isEmpty()) {
            return new ResponseEntity<>(teamsDTO, HttpStatus.NO_CONTENT);
        }

        log.debug("Posted service response for getAllTeams.");
        return ResponseEntity.ok(teamsDTO);
    }

    // [GET] View a specific Team by its ID
    @Operation(summary = "This is to view a Team by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched the Team from Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Team with ID does not exist", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable("teamId") Long teamId) {

        log.info("Received GET /api/v1/team/{} request.", teamId);

        TeamDTO teamDTO = teamService.getTeamById(teamId);

        if (teamDTO == null) {
            return new ResponseEntity("Team with id " + teamId + " is empty", HttpStatus.NOT_FOUND);
        }

        log.debug("Posted service response for getTeamById.");
        return new ResponseEntity<TeamDTO>(teamDTO, HttpStatus.OK);
    }

    // [POST] Create a Team
    @Operation(summary = "This is to create a Team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the Team and added to Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Team with the same name already exists", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/")
    public ResponseEntity createTeam(@Valid @RequestBody TeamDTO teamDTO) {

        log.info("Received POST /api/v1/team/ request.");

        teamService.createTeam(teamDTO);

        log.debug("Posted service response for createTeam.");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [PUT] Update a specific Team by its ID
    @Operation(summary = "This is to update a Team by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the Team in the Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Team with ID does not exist", content = {@Content(mediaType = "application/json")})
    })
    @PutMapping("/{teamId}")
    public ResponseEntity updateTeamById(@PathVariable("teamId") Long teamId, @Valid @RequestBody TeamDTO teamDTO) {

        log.info("Received PUT /api/v1/team/{} request.", teamId);

        teamService.updateTeamById(teamId, teamDTO);

        log.debug("Posted service response for updateTeamById.");
        return ResponseEntity.ok().build();
    }

    // [DELETE] Remove a specific Team by its ID
    @Operation(summary = "This is to delete a Team by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted the Team from Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Team with ID does not exist", content = {@Content(mediaType = "application/json")})
    })
    @DeleteMapping("/{teamId}")
    public ResponseEntity deleteTeamById(@PathVariable("teamId") Long teamId) {

        log.info("Received DELETE /api/v1/team/{} request.", teamId);

        teamService.deleteTeamById(teamId);

        log.debug("Posted service response for deleteTeamById.");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // [GET] View All Teams for League ID
    @Operation(summary = "This is to view All Teams for League ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched the Teams from League with ID from Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "No Teams from League with ID from Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "League with ID does not exist", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/league/{leagueId}")
    public ResponseEntity<List<TeamDTO>> viewAllTeamsForLeague(@PathVariable("leagueId") Long leagueId) {

        log.info("Received GET /api/v1/team/league/{} request.", leagueId);

        List<TeamDTO> teamsDTO = teamService.viewAllTeamsForLeague(leagueId);

        if (teamsDTO == null || teamsDTO.isEmpty()) {
            return new ResponseEntity<>(teamsDTO, HttpStatus.NO_CONTENT);
        }

        log.debug("Posted service response for viewAllTeamsForLeague.");
        return ResponseEntity.ok(teamsDTO);
    }

    // [PUT] Add a Team to a specific League
    @Operation(summary = "This is to add a Team to a League by their ID's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added the Team to League with ID from Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Team/League with ID does not exist", content = {@Content(mediaType = "application/json")})
    })
    @PutMapping("/{teamId}/league/{leagueId}")
    public ResponseEntity addTeamToLeague(@PathVariable("leagueId") Long leagueId,
                                          @PathVariable("teamId") Long teamId) {

        log.info("Received PUT /api/v1/team/{}/league/{} request.", teamId, leagueId);

        teamService.addTeamToLeague(leagueId, teamId);

        log.debug("Posted service response for addTeamToLeague.");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [DELETE] Remove a specific Team from a League
    @Operation(summary = "This is to remove a Team from a Leagueby its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted the Team from Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Error", content = {@Content(mediaType = "application/json")})
    })
    @DeleteMapping("/{teamId}/league/{leagueId}")
    public ResponseEntity removeTeamFromLeague(@PathVariable("leagueId") Long leagueId,
                                                   @PathVariable("teamId") Long teamId) {

        log.info("Received DELETE /api/v1/team/{}/league/{} request.", teamId, leagueId);

        teamService.removeTeamFromLeague(leagueId, teamId);

        log.debug("Posted service response for removeTeamFromLeague.");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
