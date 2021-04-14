package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.library.models.dto.xml.CharacterImportDto;
import softuni.library.models.dto.xml.CharacterRootImportDto;
import softuni.library.models.entities.Book;
import softuni.library.models.entities.Character;
import softuni.library.repositories.BookRepository;
import softuni.library.repositories.CharacterRepository;
import softuni.library.services.CharacterService;
import softuni.library.util.ValidatorUtil;
import softuni.library.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import static softuni.library.models.constants.GlobalConstants.CHARACTERS_PATH;

@Service
public class CharacterServiceImpl implements CharacterService {
    private final BookRepository bookRepo;
    private final CharacterRepository characterRepo;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;

    public CharacterServiceImpl(BookRepository bookRepo, CharacterRepository characterRepo,
                                XmlParser xmlParser, ModelMapper modelMapper,
                                ValidatorUtil validatorUtil) {
        this.bookRepo = bookRepo;
        this.characterRepo = characterRepo;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public boolean areImported() {
        return this.characterRepo.count() > 0;
    }

    @Override
    public String readCharactersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(CHARACTERS_PATH)));
    }

    @Override
    public String importCharacters() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        CharacterRootImportDto characterRootImportDto = this.xmlParser.parseXml(CharacterRootImportDto.class, CHARACTERS_PATH);

        for (CharacterImportDto importDto : characterRootImportDto.getCharacters()) {
            Optional<Book> byId = this.bookRepo.findById(importDto.getBook().getId());
            if (this.validatorUtil.isValid(importDto) && byId.isPresent()) {
                Character character = this.modelMapper.map(importDto, Character.class);

                character.setBook(this.bookRepo
                        .findById(importDto.getBook().getId())
                        .orElse(null));

                this.characterRepo.saveAndFlush(character);

                sb
                        .append(String.format("Successfully imported Character %s - age: %d",
                                importDto.getFirstName() + " " + importDto.getLastName(),
                                importDto.getAge()))
                        .append(System.lineSeparator());
            } else {
                sb.append("Invalid character").append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    @Override
    public String findCharactersInBookOrderedByLastNameDescendingThenByAge() {
        Set<Character> characters = this.characterRepo.exportCharacters(32);

        StringBuilder output = new StringBuilder();

        for (Character ch : characters) {
            output
                    .append(String.format("Character name %s, age %d, in book %s",
                            ch.getFirstName() + " " + ch.getMiddleName() + " " + ch.getLastName(),
                            ch.getAge(),
                            ch.getBook().getName()))
                    .append(System.lineSeparator());
        }

        return output.toString();
    }
}
