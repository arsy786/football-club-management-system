package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;
    private String name;
    private String city;
    private String manager;

    @ManyToOne
    @JoinColumn(name = "league_id", referencedColumnName = "leagueId") //fk
    private League league;

    @OneToMany(mappedBy = "team")
    private List<Player> players;

    @ManyToMany(mappedBy = "teams")
    private List<Cup> cups;

    @OneToOne(mappedBy = "team")
    private Stadium stadium;

    @OneToOne(mappedBy = "team")
    private Owner owner;

}
