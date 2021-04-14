package softuni.library.models.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedHashSet;
import java.util.Set;

@XmlRootElement(name = "libraries")
@XmlAccessorType(XmlAccessType.FIELD)
public class LibraryRootImportDto {
    @XmlElement(name = "library")
    private Set<LibraryImportDto> libraries = new LinkedHashSet<>();

    public LibraryRootImportDto() {
    }

    public Set<LibraryImportDto> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<LibraryImportDto> libraries) {
        this.libraries = libraries;
    }
}
