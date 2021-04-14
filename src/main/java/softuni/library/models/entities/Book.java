package softuni.library.models.entities;

import org.hibernate.validator.constraints.Length;
import org.w3c.dom.Text;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book extends BaseEntity{
    private String name;
    private int edition;
    private LocalDate written;
    private String description;
    private Author author;
    private Set<Character> characters = new LinkedHashSet<>();
    private Set<Library> libraries = new LinkedHashSet<>();

    public Book() {
    }

    @Column(name = "name", nullable = false)
    @Length(min = 5)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "edition")
    @Min(value = 1)
    @Max(value = 5)
    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    @Column(name = "written", nullable = false)
    @NotNull
    public LocalDate getWritten() {
        return written;
    }

    public void setWritten(LocalDate written) {
        this.written = written;
    }

    @Column(name = "description")
    @Length(min = 100, max = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @OneToMany(mappedBy = "book")
    public Set<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<Character> characters) {
        this.characters = characters;
    }

    @ManyToMany(mappedBy = "books")
    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }
}
