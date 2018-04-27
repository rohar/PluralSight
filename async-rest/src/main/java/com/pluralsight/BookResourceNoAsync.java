package com.pluralsight;

import org.apache.commons.codec.digest.DigestUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;

/**
 *
 */
@Path("/books_no_async")
public class BookResourceNoAsync {
    @Context BookDao dao;
    //BookDao dao = new BookDao();
    @Context
    Request request;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML + ";qs=0.5"}) // qs between 0 and 1, JSON preferred
    public Collection<Book> getBooks() {
        return dao.getBooks();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML + ";qs=0.5"}) // qs between 0 and 1, JSON preferred
    public Response getBook(@PathParam("id") String id) throws BookNotFoundException {
        Book book = dao.getBook(id);

        EntityTag entityTag = generateEntityTag(book);
        Response.ResponseBuilder rb = request.evaluatePreconditions(entityTag);
        if (rb != null) {
            // for conditional GET, hasn't been modified, so a 304 will be returned
            return rb.build();
        }

        return Response.ok().tag(entityTag).entity(book).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Book addBook(@Valid @NotNull Book book) {
        return dao.addBook(book);
    }

    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") String id, Book book) throws BookNotFoundException {
        Book originalBook = dao.getBook(id);

        Response.ResponseBuilder rb = request.evaluatePreconditions(generateEntityTag(originalBook));
        if (rb != null) {
            // for If-Match, original object has changed, so we can't patch!
            return rb.build();
        }

        Book updatedBook = dao.updateBook(id, book);
        EntityTag updatedEntityTag = generateEntityTag(updatedBook);
        return Response.ok().tag(updatedEntityTag).entity(updatedBook).build();
    }

    EntityTag generateEntityTag(Book book) {
        return new EntityTag(DigestUtils.md5Hex(book.getAuthor() + book.getTitle() + book.getPublished() + book.getExtras() + book.getIsbn()));
    }

}
