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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML + ";qs=0.5"}) // qs between 0 and 1, JSON preferred
    public Collection<Book> getBooks() {
        return dao.getBooks();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML + ";qs=0.5"}) // qs between 0 and 1, JSON preferred
    public Book getBook(@PathParam("id") String id) {
        return dao.getBook(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Book addBook(Book book) {
        return dao.addBook(book);
    }

}
