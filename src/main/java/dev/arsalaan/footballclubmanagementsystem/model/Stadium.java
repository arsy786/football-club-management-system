package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stadiumId;
    private String name;
    private Integer capacity;

    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "teamId") //fk
    private Team team;

}
