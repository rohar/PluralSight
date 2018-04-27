package com.pluralsight;

import jersey.repackaged.com.google.common.util.concurrent.ListenableFuture;
import jersey.repackaged.com.google.common.util.concurrent.ListeningExecutorService;
import jersey.repackaged.com.google.common.util.concurrent.MoreExecutors;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 *
 */
public class BookDao {
    private Map<String, Book> books;
    private ListeningExecutorService service;

    BookDao() {
        books = new ConcurrentHashMap<>();
        service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }

    ListenableFuture<Collection<Book>> getBooksAsync(final String id) {
        ListenableFuture<Collection<Book>> future =
                service.submit(new Callable<Collection<Book>>() {
                    public Collection<Book> call() throws Exception {
                        return getBooks();
                    }
                });

        return future;
    }

    Collection<Book> getBooks() {
        return books.values();
    }

    Book getBook(String id) throws BookNotFoundException {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException("Book " + id + " is not found");
        }

        return book;
    }

    ListenableFuture<Book> getBookAsync(final String id) {
        ListenableFuture<Book> future =
                service.submit(new Callable<Book>() {
                    public Book call() throws Exception {
                        return getBook(id);
                    }
                });

        return future;
    }

    Book addBook(Book book) {
        book.setId(UUID.randomUUID().toString());
        books.put(book.getId(), book);

        return book;
    }

    ListenableFuture<Book> addBookAsync(final Book book) {
        ListenableFuture<Book> future =
                service.submit(new Callable<Book>() {
                    public Book call() throws Exception {
                        return addBook(book);
                    }
                });

        return future;
    }
}
