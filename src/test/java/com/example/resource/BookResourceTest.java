package com.example.resource;

import com.example.BookUtils;
import com.example.externalService.BookExternalService;
import com.example.model.Book;
import com.example.service.BookService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class BookResourceTest {

    @InjectMock
    BookExternalService bookExternalServiceMock;

    @InjectSpy
    BookService bookServiceSpy;

    @Inject
    BookUtils bookUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExternalServiceReturnsSameResponse() {
        when(bookExternalServiceMock.getRanks("")).thenReturn(Arrays.asList(9.8, 8.7, 9.0));

        given()
                .when().get("/books/external/ranks")
                .then()
                .statusCode(200)
                .body(is("[9.8,8.7,9.0]"));

        verify(bookExternalServiceMock, times(1)).getRanks("");
    }

    @Test
    public void testExternalServiceReturnsDifferentResponses() {
        when(bookExternalServiceMock.getRanks(""))
                .thenReturn(Arrays.asList(9.8, 8.7, 9.0))
                .thenReturn(Arrays.asList(7.8, 6.7, 5.0));

        given()
                .when().get("/books/external/ranks")
                .then()
                .statusCode(200)
                .body(is("[9.8,8.7,9.0]"));

        given()
                .when().get("/books/external/ranks")
                .then()
                .statusCode(200)
                .body(is("[7.8,6.7,5.0]"));

        verify(bookExternalServiceMock, times(2)).getRanks("");
    }

    @Test
    public void testExternalServiceReturnsResponseBasedOnParameters() {
        when(bookExternalServiceMock.getRanks("ascending")).thenReturn(Arrays.asList(7.5, 8.0, 8.2));
        when(bookExternalServiceMock.getRanks("descending")).thenReturn(Arrays.asList(8.2, 8.0, 7.5));


        given()
                .when().get("/books/external/ranks?sortOder=ascending")
                .then()
                .statusCode(200)
                .body(is("[7.5,8.0,8.2]"));

        given()
                .when().get("/books/external/ranks?sortOder=descending")
                .then()
                .statusCode(200)
                .body("$", hasSize(3))
                .body(is("[8.2,8.0,7.5]"));

        verify(bookExternalServiceMock, times(1)).getRanks("ascending");
        verify(bookExternalServiceMock, times(1)).getRanks("descending");
    }

    @Test
    public void testExternalServiceThrowsError() {
        when(bookExternalServiceMock.getRanks("")).thenThrow(new RuntimeException("Service Error"));

        given()
                .when().get("/books/external/ranks")
                .then()
                .statusCode(500);

        verify(bookExternalServiceMock, times(1)).getRanks("");
    }

    @Test
    public void testServiceGetBookByIdWithSpy() {
        Book mockBook = bookUtils.createMockBook();
        when(bookServiceSpy.getBookById(1L)).thenReturn(mockBook);

        given()
                .when().get("/books/1")
                .then()
                .statusCode(200)
                .body("bookId", equalTo(1))
                .body("author", equalTo("Ion Creanga"))
                .body("title", equalTo("Amintiri din copilarie"))
                .body("publishedYear", equalTo(1875));

        verify(bookServiceSpy, times(1)).getBookById(1L);
    }

    @Test
    public void testAddBook() {
        given()
                .contentType("application/json")
                .body("{ \"bookId\": 6, \"author\": \"Author\", \"title\": \"Title\", \"isbn\": \"1234567890\", \"publishedYear\": 2024, \"rank\": 9.0 }")
                .when()
                .post("/books")
                .then()
                .statusCode(201);

        verify(bookServiceSpy, times(1)).addBook(any());
    }

    @Test
    public void testUpdateBook() {
        given()
                .contentType("application/json")
                .body("{ \"bookId\": 1, \"author\": \"New Author\", \"title\": \"New Title\", \"isbn\": \"978-0439708180\", \"publishedYear\": 1997, \"rank\": 9.8 }")
                .when()
                .put("/books/1")
                .then()
                .statusCode(200);

        verify(bookServiceSpy, times(1)).updateBook(any());
    }

    @Test
    public void testDeleteBook() {
        given()
                .when().delete("/books/1")
                .then()
                .statusCode(200);

        verify(bookServiceSpy, times(1)).deleteBook(1L);
    }
}
