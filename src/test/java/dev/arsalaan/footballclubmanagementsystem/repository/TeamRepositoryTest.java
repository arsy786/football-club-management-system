package dev.arsalaan.footballclubmanagementsystem.repository;

import dev.arsalaan.footballclubmanagementsystem.model.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Requires DB configuration in applications.properties in resource test folder.
// Need @DataJpaTest annotation (provides some standard setup needed for testing the persistence layer).
// Need @AutoConfigureTestDatabase annotation (applied to a test class to configure a test database to use instead of the application-defined or auto-configured DataSource).
// Need to @Autowired Repository (as we are testing this).


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testCreateReadDelete() {

        // given
        Team team = Team.builder()
                .teamId(1L)
                .name("Manchester United")
                .city("Manchester")
                .manager("Erik ten Hag")
                .build();

        // test create
        teamRepository.save(team);

        // test read
        Iterable<Team> teams = teamRepository.findAll();

        // assert
        Assertions.assertThat(teams).extracting(Team::getName).containsOnly("Manchester United");

        // test delete
        teamRepository.deleteAll();

        // assert
        Assertions.assertThat(teamRepository.findAll()).isEmpty();

    }

    @Test
    public void test_findTeamByName() {

        // given
        Team team = Team.builder()
                .teamId(1L)
                .name("Manchester United")
                .city("Manchester")
                .manager("Erik ten Hag")
                .build();
        teamRepository.save(team);

        // when
        Optional<Team> teamOptional = teamRepository.findTeamByName("Manchester United");
        boolean teamIsPresent = teamOptional.isPresent();

        // then
        assertTrue(teamIsPresent);
        Assertions.assertThat(teamOptional.get().getCity()).isEqualTo("Manchester");
    }


}
