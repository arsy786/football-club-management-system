package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.StadiumDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.StadiumMapper;
import dev.arsalaan.footballclubmanagementsystem.model.Stadium;
import dev.arsalaan.footballclubmanagementsystem.repository.StadiumRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StadiumService {

    private final StadiumRepository stadiumRepository;
    private final StadiumMapper stadiumMapper;

    public StadiumService(StadiumRepository stadiumRepository, StadiumMapper stadiumMapper) {
        this.stadiumRepository = stadiumRepository;
        this.stadiumMapper = stadiumMapper;
    }

    public List<StadiumDTO> getAllStadiums() {
        List<Stadium> stadiums = stadiumRepository.findAll();
        return stadiumMapper.toStadiumDTOs(stadiums);
    }

    public StadiumDTO getStadiumById(Long stadiumId) {

        Stadium stadium = stadiumRepository.findById(stadiumId).orElseThrow(
                () -> new ApiRequestException("stadium with id " + stadiumId + " does not exist"));

        return stadiumMapper.toStadiumDTO(stadium);
    }

    public void createStadium(StadiumDTO stadiumDTO) {

        Stadium stadium = stadiumMapper.toStadium(stadiumDTO);

        // check if Stadium name exists
        Optional<Stadium> stadiumOptional = stadiumRepository.findStadiumByName(stadium.getName());

        if (stadiumOptional.isPresent()) {
            throw new ApiRequestException("stadium name " + stadium.getName() + " already exists");
        }

        stadiumRepository.save(stadium);
    }

    @Transactional
    public void updateStadiumById(Long stadiumId, StadiumDTO stadiumDTO) {

        Stadium stadium = stadiumRepository.findById(stadiumId).orElseThrow(
                () -> new ApiRequestException("stadium with id " + stadiumId + " does not exist"));

        if (stadiumDTO.getName() != null && stadiumDTO.getName().length() > 0 && !Objects.equals(stadium.getName(), stadiumDTO.getName())) {
            stadium.setName(stadiumDTO.getName());
        }

        if (stadiumDTO.getCapacity() != null && stadiumDTO.getCapacity() > 0 && !Objects.equals(stadium.getCapacity(), stadiumDTO.getCapacity())) {
            stadium.setCapacity(stadiumDTO.getCapacity());
        }

    }

    public void deleteStadiumById(Long stadiumId) {

        boolean exists = stadiumRepository.existsById(stadiumId);

        if (!exists) {
            throw new ApiRequestException("stadium with id " + stadiumId + " does not exist");
        }

        stadiumRepository.deleteById(stadiumId);
    }

}
