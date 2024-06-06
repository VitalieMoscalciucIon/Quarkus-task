package com.example.repository;

import com.example.model.Book;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookRepository {

    private static final List<Book> books = new ArrayList<>();

    static {
        books.add(new Book(1L, "J.K. Rowling", "Harry Potter and the Sorcerer's Stone", "978-0439708180", 1997,9.8));
        books.add(new Book(2L, "J.R.R. Tolkien", "The Hobbit", "978-0345339683", 1937,8.7));
        books.add(new Book(3L, "George Orwell", "1984", "978-0451524935", 1949,9.0));
        books.add(new Book(4L, "F. Scott Fitzgerald", "The Great Gatsby", "978-0743273565", 1925,8.5));
        books.add(new Book(5L, "Harper Lee", "To Kill a Mockingbird", "978-0061120084", 1960,7.9));
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Book getBookById(long id) {
        Optional<Book> book = getAllBooks().stream()
                .filter(b -> b.getBookId().equals(id))
                .findFirst();
        return book.orElse(null);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void updateBook(Book book) {
        Book oldBook = getBookById(book.getBookId());
        if (oldBook != null) {
            oldBook.setAuthor(book.getAuthor());
            oldBook.setTitle(book.getTitle());
            oldBook.setISBN(book.getISBN());
            oldBook.setPublishedYear(book.getPublishedYear());
        }
    }

    public void deleteBook(long id) {
        books.removeIf(book -> book.getBookId().equals(id));
    }
}

