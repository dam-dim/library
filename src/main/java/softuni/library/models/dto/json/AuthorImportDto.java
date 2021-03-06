package softuni.library.models.dto.json;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AuthorImportDto {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String birthTown;

    public AuthorImportDto() {
    }

    @Length(min = 2)
    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Length(min = 2)
    @NotNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Length(min = 4, max = 11)
    @NotNull
    public String getBirthTown() {
        return birthTown;
    }

    public void setBirthTown(String birthTown) {
        this.birthTown = birthTown;
    }
}
