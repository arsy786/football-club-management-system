package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "manager")
    private String manager;

    @ManyToOne
    @JoinColumn(name = "league_id", referencedColumnName = "leagueId") //fk
    private League league;

    @OneToMany(mappedBy = "team")
    @ToString.Exclude
    private List<Player> players;

    @ManyToMany(mappedBy = "teams")
    @ToString.Exclude
    private List<Cup> cups;

    @OneToOne(mappedBy = "team")
    private Stadium stadium;

    @OneToOne(mappedBy = "team")
    private Owner owner;

}
