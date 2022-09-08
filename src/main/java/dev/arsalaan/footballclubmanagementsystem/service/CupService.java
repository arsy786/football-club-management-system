package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.CupDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.CupMapper;
import dev.arsalaan.footballclubmanagementsystem.model.Cup;
import dev.arsalaan.footballclubmanagementsystem.repository.CupRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CupService {

    private final CupRepository cupRepository;
    private final CupMapper cupMapper;

    public CupService(CupRepository cupRepository, CupMapper cupMapper) {
        this.cupRepository = cupRepository;
        this.cupMapper = cupMapper;
    }

    public List<CupDTO> getAllCups() {
        List<Cup> cups = cupRepository.findAll();
        return cupMapper.toCupDTOs(cups);
    }

    public CupDTO getCupById(Long cupId) {

        Cup cup = cupRepository.findById(cupId).orElseThrow(
                () -> new ApiRequestException("cup with id " + cupId + " does not exist"));

        return cupMapper.toCupDTO(cup);
    }

    public void createCup(CupDTO cupDTO) {

        Cup cup = cupMapper.toCup(cupDTO);

        // check if Cup name exists
        Optional<Cup> cupOptional = cupRepository.findCupByName(cup.getName());

        if (cupOptional.isPresent()) {
            throw new ApiRequestException("cup name " + cup.getName() + " already exists");
        }

        cupRepository.save(cup);
    }

    @Transactional
    public void updateCupById(Long cupId, CupDTO cupDTO) {

        Cup cup = cupRepository.findById(cupId).orElseThrow(
                () -> new ApiRequestException("cup with id " + cupId + " does not exist"));

        if (cupDTO.getName() != null && cupDTO.getName().length() > 0 && !Objects.equals(cup.getName(), cupDTO.getName())) {
            cup.setName(cupDTO.getName());
        }

        if (cupDTO.getNumberOfTeams() != null && cupDTO.getNumberOfTeams() > 0 && !Objects.equals(cup.getNumberOfTeams(), cupDTO.getNumberOfTeams())) {
            cup.setNumberOfTeams(cupDTO.getNumberOfTeams());
        }

    }

    public void deleteCupById(Long cupId) {

        boolean exists = cupRepository.existsById(cupId);

        if (!exists) {
            throw new ApiRequestException("cup with id " + cupId + " does not exist");
        }

        cupRepository.deleteById(cupId);
    }

}
