package dev.arsalaan.footballclubmanagementsystem.controller;

import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

        List<TeamDTO> teamsDTO = teamService.getAllTeams();

        if (teamsDTO == null || teamsDTO.isEmpty()) {
            return new ResponseEntity<>(teamsDTO, HttpStatus.NO_CONTENT);
        }

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

        TeamDTO teamDTO = teamService.getTeamById(teamId);

        if (teamDTO == null) {
            return new ResponseEntity("Team with id " + teamId + " is empty", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<TeamDTO>(teamDTO, HttpStatus.OK);
    }

    // [POST] Create a Team
    @Operation(summary = "This is to create a Team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the Team and added to Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Team with the same name already exists", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/")
    public ResponseEntity createTeam(/*@Valid*/ @RequestBody TeamDTO teamDTO) {
        teamService.createTeam(teamDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [PUT] Update a specific Team by its ID
    @Operation(summary = "This is to update a Team by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the Team in the Db", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Team with ID does not exist", content = {@Content(mediaType = "application/json")})
    })
    @PutMapping("/{teamId}")
    public ResponseEntity updateTeamById(@PathVariable("teamId") Long teamId, @RequestBody TeamDTO teamDTO) {
        teamService.updateTeamById(teamId, teamDTO);
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
        teamService.deleteTeamById(teamId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
