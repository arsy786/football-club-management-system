package dev.arsalaan.footballclubmanagementsystem.mapper;

import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.model.Team;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDTO toTeamDTO (Team team);

    List<TeamDTO> toTeamDTOs(List<Team> teams);

    Team toTeam(TeamDTO teamDTO);

}
