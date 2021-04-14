package softuni.library.models.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "libraries")
public class Library extends BaseEntity{
    private String name;
    private String location;
    private int rating;
    private Set<Book> books = new LinkedHashSet<>();

    public Library() {
    }

    @Column(name = "name", nullable = false, unique = true)
    @Length(min = 3)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "location", nullable = false)
    @Length(min = 5)
    @NotNull
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "reating")
    @Min(value = 1)
    @Max(value = 10)
    public int getRating() {
        return rating;
    }

    public void setRating(int reating) {
        this.rating = reating;
    }

    @ManyToMany
    @JoinTable(
            name = "libraries_books",
            joinColumns = @JoinColumn(name = "library_id"),
            inverseJoinColumns = @JoinColumn(name = "books_id")
    )
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
