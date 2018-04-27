package com.pluralsight;

import jersey.repackaged.com.google.common.util.concurrent.FutureCallback;
import jersey.repackaged.com.google.common.util.concurrent.Futures;
import jersey.repackaged.com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.codec.digest.DigestUtils;
import org.glassfish.jersey.server.ManagedAsync;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;

/**
 *
 */
@Path("/books")
public class BookResource {
    @Context BookDao dao;
    //BookDao dao = new BookDao();
    @Context Request request;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML + ";qs=0.5"}) // qs between 0 and 1, JSON preferred
    @ManagedAsync
    public void getBooks(@Suspended final AsyncResponse response) {
        response.resume(dao.getBooks());
    }

    @PoweredBy("Pluralsight")
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML + ";qs=0.5"}) // qs between 0 and 1, JSON preferred
    @ManagedAsync
    public void getBook(@PathParam("id") String id, @Suspended final AsyncResponse response) {
        //return dao.getBook(id);
        ListenableFuture<Book> bookFuture = dao.getBookAsync(id);
        Futures.addCallback(bookFuture, new FutureCallback<Book>() {
            @Override
            public void onSuccess(Book book) {
                EntityTag entityTag = generateEntityTag(book);
                Response.ResponseBuilder rb = request.evaluatePreconditions(entityTag);
                if (rb != null) {
                    // for conditional GET, hasn't been modified, so a 304 will be returned
                    response.resume(rb.build());
                } else {
                    response.resume(Response.ok().tag(entityTag).entity(book).build());
                }
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void addBook(@Valid @NotNull Book book, @Suspended AsyncResponse response) {
        ListenableFuture<Book> bookFuture = dao.addBookAsync(book);
        Futures.addCallback(bookFuture, new FutureCallback<Book>() {
            @Override
            public void onSuccess(Book addedBook) {
                response.resume(addedBook);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void updateBook(@PathParam("id") final String id, final Book book, @Suspended final AsyncResponse response) {

        ListenableFuture<Book> bookFuture = dao.getBookAsync(id);
        Futures.addCallback(bookFuture, new FutureCallback<Book>() {
            @Override
            public void onSuccess(Book originalBook) {
                Response.ResponseBuilder rb = request.evaluatePreconditions(generateEntityTag(originalBook));
                if (rb != null) {
                    // for If-Match, original object has changed, so we can't patch!
                    response.resume(rb.build());
                } else {
                    ListenableFuture<Book> bookFuture = dao.updateBookAsync(id, book);
                    Futures.addCallback(bookFuture, new FutureCallback<Book>() {
                        @Override
                        public void onSuccess(Book updatedBook) {
                            EntityTag updatedEntityTag = generateEntityTag(updatedBook);
                            response.resume(Response.ok().tag(updatedEntityTag).entity(updatedBook).build());
                        }

                        @Override
                        public void onFailure(Throwable thrown) {
                            response.resume(thrown);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    EntityTag generateEntityTag(Book book) {
        return new EntityTag(DigestUtils.md5Hex(book.getAuthor() + book.getTitle() + book.getPublished() + book.getExtras() + book.getIsbn()));
    }
}
