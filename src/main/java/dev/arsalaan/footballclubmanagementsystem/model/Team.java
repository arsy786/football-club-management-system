package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
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
    private List<Player> players;

    @ManyToMany(mappedBy = "teams")
    private List<Cup> cups;

    @OneToOne(mappedBy = "team")
    private Stadium stadium;

    @OneToOne(mappedBy = "team")
    private Owner owner;

}
