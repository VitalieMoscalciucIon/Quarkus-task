package com.example.resource;

import com.example.externalService.BookExternalService;
import com.example.model.Book;
import com.example.service.BookService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.util.List;

@Path("/books")
public class BookResource {

    @Inject
    BookService bookService;

    @Inject
    BookExternalService bookExternalService;

    public BookResource() {
        this.bookService = new BookService();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        return Response.ok(bookService.getAllBooks()).build();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") long id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return Response.ok(book).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response addBook(Book book) {
        bookService.addBook(book);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") long id, Book book) {
        book.setBookId(id);
        bookService.updateBook(book);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") long id) {
        bookService.deleteBook(id);
        return Response.ok().build();
    }

    @GET
    @Path("/external/ranks/")
    public List<Double> getRanksFromExternalService(@QueryParam("sortOder") @DefaultValue("") String sortOrder) {
        return bookExternalService.getRanks(sortOrder);
    }

    @GET
    @Path("/external/highest-rank")
    public Response getHighestRankFromExternalService() {
        return Response.ok(bookExternalService.getBookNameWithHighestRank()).build();
    }
}
