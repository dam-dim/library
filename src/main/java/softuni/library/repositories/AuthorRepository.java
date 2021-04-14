package softuni.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.library.models.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
