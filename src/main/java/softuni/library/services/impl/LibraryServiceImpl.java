package softuni.library.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.library.models.dto.xml.LibraryImportDto;
import softuni.library.models.dto.xml.LibraryRootImportDto;
import softuni.library.models.entities.Book;
import softuni.library.models.entities.Library;
import softuni.library.repositories.BookRepository;
import softuni.library.repositories.LibraryRepository;
import softuni.library.services.LibraryService;
import softuni.library.util.ValidatorUtil;
import softuni.library.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static softuni.library.models.constants.GlobalConstants.CHARACTERS_PATH;
import static softuni.library.models.constants.GlobalConstants.LIBRARIES_PATH;

@Service
public class LibraryServiceImpl implements LibraryService {
    private final LibraryRepository libraryRepo;
    private final BookRepository bookRepo;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;

    public LibraryServiceImpl(LibraryRepository libraryRepo, BookRepository bookRepo, XmlParser xmlParser, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.libraryRepo = libraryRepo;
        this.bookRepo = bookRepo;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public boolean areImported() {
        return this.libraryRepo.count() > 0;
    }

    @Override
    public String readLibrariesFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(LIBRARIES_PATH)));
    }

    @Override
    public String importLibraries() throws JAXBException {
       StringBuilder sb = new StringBuilder();

        LibraryRootImportDto libraryRootImportDto = this.xmlParser.parseXml(LibraryRootImportDto.class, LIBRARIES_PATH);

        for (LibraryImportDto importDto : libraryRootImportDto.getLibraries()) {
            Optional<Library> byName = this.libraryRepo.getByName(importDto.getName());
            Optional<Book> byId = this.bookRepo.findById(importDto.getBook().getId());

            if(this.validatorUtil.isValid(importDto) && byName.isEmpty()) {
                Library library = this.modelMapper.map(importDto, Library.class);

                if (this.bookRepo.findById(importDto.getBook().getId()).isPresent()) {
                    library.setBooks(Set.of(this.bookRepo.getOne(importDto.getBook().getId())));
                }

                this.libraryRepo.saveAndFlush(library);

                sb
                        .append(String.format("Successfully added Library: %s - %s",
                                importDto.getName(),
                                importDto.getLocation()))
                        .append(System.lineSeparator());
            } else {
                sb.append("Invalid library").append(System.lineSeparator());
            }

        }

        return sb.toString();
    }
}
