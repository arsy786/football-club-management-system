package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
