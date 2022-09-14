package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownerId;
    private String name;
    private String netWorth;

    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "teamId") //fk
    private Team team;

}
