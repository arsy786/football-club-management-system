package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leagueId;
    private String name;
    private String country;
    private Integer numberOfTeams;

    @OneToMany(mappedBy = "league")
    private List<Team> teams;

}
