package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.library.models.dto.json.AuthorImportDto;
import softuni.library.models.entities.Author;
import softuni.library.repositories.AuthorRepository;
import softuni.library.services.AuthorService;
import softuni.library.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.library.models.constants.GlobalConstants.AUTHORS_PATH;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepo;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;

    public AuthorServiceImpl(AuthorRepository authorRepo, Gson gson, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.authorRepo = authorRepo;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public boolean areImported() {
        return this.authorRepo.count() > 0;
    }

    @Override
    public String readAuthorsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(AUTHORS_PATH)));
    }

    @Override
    public String importAuthors() throws IOException {
        StringBuilder sb = new StringBuilder();

        AuthorImportDto[] authorImportDtos = this.gson
                .fromJson(readAuthorsFileContent(), AuthorImportDto[].class);

        for (AuthorImportDto authorImportDto : authorImportDtos) {
            if(this.validatorUtil.isValid(authorImportDto)) {
                this.authorRepo.saveAndFlush(this.modelMapper.map(authorImportDto, Author.class));

                sb.append(String.format("Successfully imported Author: %s %s - %s",
                        authorImportDto.getFirstName(),
                        authorImportDto.getLastName(),
                        authorImportDto.getBirthTown()));
                sb.append(System.lineSeparator());
            } else {
                sb.append("Invalid Author").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
