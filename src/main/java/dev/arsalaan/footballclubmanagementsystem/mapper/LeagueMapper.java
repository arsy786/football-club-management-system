package dev.arsalaan.footballclubmanagementsystem.mapper;

import dev.arsalaan.footballclubmanagementsystem.dto.LeagueDTO;
import dev.arsalaan.footballclubmanagementsystem.model.League;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeagueMapper {

    LeagueDTO toLeagueDTO (League league);

    List<LeagueDTO> toLeagueDTOs(List<League> leagues);

    League toLeague(LeagueDTO leagueDTO);

}
