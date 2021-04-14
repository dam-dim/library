package softuni.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.library.models.entities.Library;

import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    Optional<Library> getByName(String name);
}
