package softuni.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import softuni.library.models.entities.Character;

import java.util.Set;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    @Query("SELECT c FROM Character c " +
            "WHERE c.age >= :age " +
            "ORDER BY c.book.name, c.lastName DESC, c.age")
    Set<Character> exportCharacters(@Param("age")int age);
}
