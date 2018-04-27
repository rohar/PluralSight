package com.pluralsight;

import jersey.repackaged.com.google.common.util.concurrent.FutureCallback;
import jersey.repackaged.com.google.common.util.concurrent.Futures;
import jersey.repackaged.com.google.common.util.concurrent.ListenableFuture;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 */
@Path("/books")
public class BookResource {
    @Context
    BookDao dao;
    //BookDao dao = new BookDao();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML + ";qs=0.5"}) // qs between 0 and 1, JSON preferred
    @ManagedAsync
    public void getBooks(@Suspended final AsyncResponse response) {
        response.resume(dao.getBooks());
    }

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
                response.resume(book);
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
    public void addBook(Book book, @Suspended AsyncResponse response) {
        //response.resume(dao.addBook(book));
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
}
