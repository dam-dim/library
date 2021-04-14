package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.library.models.dto.json.BookImportDto;
import softuni.library.models.entities.Author;
import softuni.library.models.entities.Book;
import softuni.library.repositories.AuthorRepository;
import softuni.library.repositories.BookRepository;
import softuni.library.services.BookService;
import softuni.library.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static softuni.library.models.constants.GlobalConstants.AUTHORS_PATH;
import static softuni.library.models.constants.GlobalConstants.BOOKS_PATH;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;

    public BookServiceImpl(BookRepository bookRepo, AuthorRepository authorRepo, Gson gson, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public boolean areImported() {
        return this.bookRepo.count() > 0;
    }

    @Override
    public String readBooksFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(BOOKS_PATH)));
    }

    @Override
    public String importBooks() throws IOException {
       StringBuilder sb = new StringBuilder();

        BookImportDto[] bookImportDto = this.gson.fromJson(this.readBooksFileContent(), BookImportDto[].class);

        for (BookImportDto importDto : bookImportDto) {
            if (this.validatorUtil.isValid(importDto)) {
                Book book = this.modelMapper.map(importDto, Book.class);

                book.setAuthor(this.authorRepo
                        .findById(importDto.getAuthor())
                        .orElse(null));

                this.bookRepo.saveAndFlush(book);

                sb.append(String.format("Successfully imported Book: %s written in %s",
                        importDto.getName(),
                        importDto.getWritten()));
                sb.append(System.lineSeparator());

            } else {
                sb.append("Invalid Book").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
