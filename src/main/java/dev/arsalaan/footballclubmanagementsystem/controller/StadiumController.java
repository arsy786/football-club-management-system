package dev.arsalaan.footballclubmanagementsystem.controller;

import dev.arsalaan.footballclubmanagementsystem.dto.StadiumDTO;
import dev.arsalaan.footballclubmanagementsystem.service.StadiumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stadium")
public class StadiumController {

    private final StadiumService stadiumService;

    public StadiumController(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }

    // [GET] View All Stadiums
    @GetMapping("/")
    public ResponseEntity<List<StadiumDTO>> getAllStadiums() {

        List<StadiumDTO> stadiumsDTO = stadiumService.getAllStadiums();

        if (stadiumsDTO == null || stadiumsDTO.isEmpty()) {
            return new ResponseEntity<>(stadiumsDTO, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(stadiumsDTO);
    }

    // [GET] View a specific Stadium by its ID
    @GetMapping("/{stadiumId}")
    public ResponseEntity<StadiumDTO> getStadiumById(@PathVariable("stadiumId") Long stadiumId) {

        StadiumDTO stadiumDTO = stadiumService.getStadiumById(stadiumId);

        if (stadiumDTO == null) {
            return new ResponseEntity("Stadium with id " + stadiumId + " does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<StadiumDTO>(stadiumDTO, HttpStatus.OK);
    }

    // [POST] Create a Stadium
    @PostMapping("/")
    public ResponseEntity createStadium(/*@Valid*/ @RequestBody StadiumDTO stadiumDTO) {
        stadiumService.createStadium(stadiumDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [PUT] Update a specific Stadium by its ID
    @PutMapping("/{stadiumId}")
    public ResponseEntity updateStadiumById(@PathVariable("stadiumId") Long stadiumId, @RequestBody StadiumDTO stadiumDTO) {
        stadiumService.updateStadiumById(stadiumId, stadiumDTO);
        return ResponseEntity.ok().build();
    }

    // [DELETE] Remove a specific Stadium by its ID
    @DeleteMapping("/{stadiumId}")
    public ResponseEntity deleteStadiumById(@PathVariable("stadiumId") Long stadiumId) {
        stadiumService.deleteStadiumById(stadiumId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
