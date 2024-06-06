package com.example;

import com.example.model.Book;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class BookUtils {

    public Book createMockBook(){
        return Book.builder()
                .bookId(1L)
                .title("Amintiri din copilarie")
                .author("Ion Creanga")
                .ISBN("9875987595")
                .publishedYear(1875)
                .rank(8.2)
                .build();
    }

    public List<Book> getMockBooks() {
        return Arrays.asList(
                new Book(1L, "J.K. Rowling", "Harry Potter and the Sorcerer's Stone", "978-0439708180", 1997, 9.8),
                new Book(2L, "J.R.R. Tolkien", "The Hobbit", "978-0345339683", 1937, 8.7)
        );
    }

}
