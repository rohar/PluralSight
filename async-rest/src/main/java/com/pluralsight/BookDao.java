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

    Book updateBook(String id, Book updates) throws BookNotFoundException {
        Book book = books.get(id);

        if (book == null) {
            throw new BookNotFoundException("Book " + id + " is not found");
        }

        if (updates.getPublished() != null) {
            book.setPublished(updates.getPublished());
        }
        if (updates.getTitle() != null) {
            book.setTitle(updates.getTitle());
        }
        if (updates.getAuthor() != null) {
            book.setAuthor(updates.getAuthor());
        }
        if (updates.getIsbn() != null) {
            book.setIsbn(updates.getIsbn());
        }
        if (updates.getExtras() != null) {
            for (String key : updates.getExtras().keySet()) {
                book.set(key, updates.getExtras().get(key));
            }
        }

        return book;
    }

    ListenableFuture<Book> updateBookAsync(final String id, final Book book) {
        ListenableFuture<Book> future =
                service.submit(new Callable<Book> () {
                    public Book call() throws Exception {
                        return updateBook(id, book);
                    }
                });

        return future;
    }
}
