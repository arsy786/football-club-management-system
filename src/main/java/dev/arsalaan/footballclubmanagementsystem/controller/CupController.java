package dev.arsalaan.footballclubmanagementsystem.controller;

import dev.arsalaan.footballclubmanagementsystem.dto.CupDTO;
import dev.arsalaan.footballclubmanagementsystem.service.CupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cup")
public class CupController {

    private final CupService cupService;

    public CupController(CupService cupService) {
        this.cupService = cupService;
    }

    // [GET] View All Cups
    @GetMapping("/")
    public ResponseEntity<List<CupDTO>> getAllCups() {

        List<CupDTO> cupsDTO = cupService.getAllCups();

        if (cupsDTO == null || cupsDTO.isEmpty()) {
            return new ResponseEntity<>(cupsDTO, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(cupsDTO);
    }

    // [GET] View a specific Cup by its ID
    @GetMapping("/{cupId}")
    public ResponseEntity<CupDTO> getCupById(@PathVariable("cupId") Long cupId) {

        CupDTO cupDTO = cupService.getCupById(cupId);

        if (cupDTO == null) {
            return new ResponseEntity("Cup with id " + cupId + " does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<CupDTO>(cupDTO, HttpStatus.OK);
    }

    // [POST] Create a Cup
    @PostMapping("/")
    public ResponseEntity createCup(/*@Valid*/ @RequestBody CupDTO cupDTO) {
        cupService.createCup(cupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [PUT] Update a specific Cup by its ID
    @PutMapping("/{cupId}")
    public ResponseEntity updateCupById(@PathVariable("cupId") Long cupId, @RequestBody CupDTO cupDTO) {
        cupService.updateCupById(cupId, cupDTO);
        return ResponseEntity.ok().build();
    }

    // [DELETE] Remove a specific Cup by its ID
    @DeleteMapping("/{cupId}")
    public ResponseEntity deleteCupById(@PathVariable("cupId") Long cupId) {
        cupService.deleteCupById(cupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
