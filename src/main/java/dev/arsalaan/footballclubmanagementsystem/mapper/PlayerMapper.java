package dev.arsalaan.footballclubmanagementsystem.mapper;

import dev.arsalaan.footballclubmanagementsystem.dto.PlayerDTO;
import dev.arsalaan.footballclubmanagementsystem.model.Player;
import dev.arsalaan.footballclubmanagementsystem.model.Team;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerDTO toPlayerDTO (Player player);

    List<PlayerDTO> toPlayerDTOs(List<Team> teams);

    Player toPlayer(PlayerDTO playerDTO);

}
