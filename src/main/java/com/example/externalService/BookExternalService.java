package com.example.externalService;

import com.example.model.Book;
import com.example.service.BookService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;

@ApplicationScoped
public class BookExternalService {

    @Inject
    BookService bookService;

    private static final List<Double> STATIC_RANKS = Arrays.asList(8.5, 8.8, 8.2, 9.0, 8.1);

    public List<Double> getRanks(String sortOrder) {
        if ("descending".equalsIgnoreCase(sortOrder)) {
            return STATIC_RANKS.stream()
                    .sorted(Collections.reverseOrder())
                    .toList();
        } else if ("ascending".equalsIgnoreCase(sortOrder)) {
            return STATIC_RANKS.stream()
                    .sorted()
                    .toList();
        } else {
            return STATIC_RANKS;
        }
    }

    public String getBookNameWithHighestRank() {
        Optional<Book> bookWithHighestRank = bookService.getAllBooks().stream()
                .max(Comparator.comparingDouble(Book::getRank));
        return bookWithHighestRank.map(Book::getTitle).orElse(null);
    }
}
