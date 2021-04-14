package softuni.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.library.models.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
