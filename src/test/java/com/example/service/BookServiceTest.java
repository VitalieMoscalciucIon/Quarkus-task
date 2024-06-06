package com.example.service;

import com.example.BookUtils;
import com.example.model.Book;
import com.example.repository.BookRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class BookServiceTest {

    @InjectMock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    @Inject
    BookUtils bookUtils;

    List<Book> mockBooks;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockBooks = bookUtils.getMockBooks();
    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.getAllBooks()).thenReturn(mockBooks);
        List<Book> books = bookService.getAllBooks();
        assertNotNull(books);
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).getAllBooks();
    }

    @Test
    public void testGetBookById() {
        Book mockBook = new Book(1L, "J.K. Rowling", "Harry Potter and the Sorcerer's Stone", "978-0439708180", 1997, 9.8);

        when(bookRepository.getBookById(1L)).thenReturn(mockBook);

        Book book = bookService.getBookById(1L);
        assertNotNull(book);
        assertEquals("Harry Potter and the Sorcerer's Stone", book.getTitle());
        verify(bookRepository, times(1)).getBookById(1L);
    }

    @Test
    public void testAddBook() {
        when(bookRepository.getAllBooks()).thenReturn(bookUtils.getMockBooks());
        Book newBook = new Book(3L, "George Orwell", "1984", "978-0451524935", 1949, 9.0);

        bookService.addBook(newBook);
        verify(bookRepository, times(1)).addBook(newBook);
    }

    @Test
    public void testUpdateBook() {
        Book existingBook = new Book(1L, "J.K. Rowling", "Harry Potter and the Sorcerer's Stone", "978-0439708180", 1997, 9.8);

        bookService.updateBook(existingBook);
        verify(bookRepository, times(1)).updateBook(existingBook);
    }

    @Test
    public void testDeleteBook() {
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteBook(1L);
    }
}
