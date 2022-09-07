package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.LeagueDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.LeagueMapper;
import dev.arsalaan.footballclubmanagementsystem.model.League;
import dev.arsalaan.footballclubmanagementsystem.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final LeagueMapper leagueMapper;

    public LeagueService(LeagueRepository leagueRepository, LeagueMapper leagueMapper) {
        this.leagueRepository = leagueRepository;
        this.leagueMapper = leagueMapper;
    }

    public List<LeagueDTO> getAllLeagues() {
        List<League> leagues = leagueRepository.findAll();
        return leagueMapper.toLeagueDTOs(leagues);
    }

    public LeagueDTO getLeagueById(Long leagueId) {

        League league = leagueRepository.findById(leagueId).orElseThrow(
                () -> new ApiRequestException("league with id " + leagueId + " does not exist"));

        return leagueMapper.toLeagueDTO(league);
    }

    public void createLeague(LeagueDTO leagueDTO) {

        League league = leagueMapper.toLeague(leagueDTO);

        // check if League name exists
        Optional<League> leagueOptional = leagueRepository.findLeagueByName(league.getName());

        if (leagueOptional.isPresent()) {
            throw new ApiRequestException("league name " + league.getName() + " already exists");
        }

        leagueRepository.save(league);
    }

    @Transactional
    public void updateLeagueById(Long leagueId, String name, String country, Integer numberOfTeams) {

        League league = leagueRepository.findById(leagueId).orElseThrow(
                () -> new ApiRequestException("league with id " + leagueId + " does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(league.getName(), name)) {
            league.setName(name);
        }

        if (country != null && country.length() > 0 && !Objects.equals(league.getCountry(), country)) {
            league.setCountry(country);
        }


        if (numberOfTeams != null && numberOfTeams > 0 && !Objects.equals(league.getNumberOfTeams(), numberOfTeams)) {
            league.setNumberOfTeams(numberOfTeams);
        }

    }

    public void deleteLeagueById(Long leagueId) {

        boolean exists = leagueRepository.existsById(leagueId);

        if (!exists) {
            throw new ApiRequestException("league with id " + leagueId + " does not exist");
        }

        leagueRepository.deleteById(leagueId);
    }

}
