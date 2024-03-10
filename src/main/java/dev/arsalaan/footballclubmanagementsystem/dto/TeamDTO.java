package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor // Generates no-args constructor
@AllArgsConstructor // Generates all-args constructor
@Builder
public class TeamDTO implements Serializable {

    private Long teamId;

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "City should not be null or empty")
    private String city;

    private String manager;

    private List<PlayerDTO> players;

    @Data
    @AllArgsConstructor // Necessary for the immutable PlayerDTO
    public static class PlayerDTO implements Serializable {
        private final String name;
    }

}
