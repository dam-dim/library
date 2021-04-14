package softuni.library.models.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedHashSet;
import java.util.Set;

@XmlRootElement(name = "characters")
@XmlAccessorType(XmlAccessType.FIELD)
public class CharacterRootImportDto {
    @XmlElement(name = "character")
    private Set<CharacterImportDto> characters = new LinkedHashSet<>();

    public CharacterRootImportDto() {
    }

    public Set<CharacterImportDto> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<CharacterImportDto> characters) {
        this.characters = characters;
    }
}
