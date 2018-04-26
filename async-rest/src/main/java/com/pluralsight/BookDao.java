package com.pluralsight;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class BookDao {
    private Map<String, Book> books;

    BookDao() {
        books = new ConcurrentHashMap<>();

    }

    Collection<Book> getBooks() {
        return books.values();
    }

    Book getBook(String id) {
        return books.get(id);
    }

    Book addBook(Book book) {
        book.setId(UUID.randomUUID().toString());
        books.put(book.getId(), book);

        return book;
    }
}
