package com.pluralsight;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 *
 */
@Path("/books_no_async")
public class BookResourceNoAsync {
    @Context
    BookDao dao;
    //BookDao dao = new BookDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Book> getBooks() {
        return dao.getBooks();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBookAsync(@PathParam("id") String id) {
        return dao.getBook(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Book addBookAsync(Book book) {
        return dao.addBook(book);
    }

}
