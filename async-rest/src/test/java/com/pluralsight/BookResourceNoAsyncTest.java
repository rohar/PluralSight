package com.pluralsight;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookResourceNoAsyncTest extends JerseyTest {
    private String book1_id;
    private String book2_id;

    @Override
    protected Application configure() {
//        enable(TestProperties.LOG_TRAFFIC);
//        enable(TestProperties.DUMP_ENTITY);

        final BookDao dao = new BookDao();

        return new BookApplication(dao);
    }

    /**
     * Ensure null values don't appear in maps
     * @param clientConfig
     */
    protected void configureClient(ClientConfig clientConfig) {
        JacksonJsonProvider json = new JacksonJsonProvider();
        json.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        clientConfig.register(json);
        clientConfig.connectorProvider(new GrizzlyConnectorProvider());
    }

    protected Response addBook(String author, String title, Date published, String isbn, String... extras) {
        HashMap<String, Object> book = new HashMap<>();
        book.put("author", author);
        book.put("title", title);
        book.put("published", published);
        book.put("isbn", isbn);
        if (extras != null) {
            int count = 1;
            for (String s : extras) {
                book.put("extra" + count++, s);
            }
        }

        Entity<HashMap<String, Object>> bookEntity = Entity.entity(book, MediaType.APPLICATION_JSON);
        return target("books_no_async").request().post(bookEntity);
    }

    protected Map<String, Object> toHashMap(Response response) {
        return response.readEntity(new GenericType<HashMap<String, Object>>() {});
    }

    @Test
    public void testAddBook() throws Exception {
        Date thisDate = new Date();

        Response response = addBook("author", "title", thisDate, "4321");

        assertEquals(200, response.getStatus());

        Map<String, Object> responseBook = toHashMap(response);
        assertNotNull(responseBook.get("id"));
        assertEquals("title", responseBook.get("title"));
        assertEquals("author", responseBook.get("author"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        assertEquals(thisDate, dateFormat.parse((String)responseBook.get("published")));
        assertEquals("4321", responseBook.get("isbn"));
    }

    @Before
    public void setupBooks() {
        book1_id = (String)toHashMap(addBook("author1", "title1", new Date(), "1234")).get("id");
        book2_id = (String)toHashMap(addBook("author2", "title2", new Date(), "2345")).get("id");
    }

    @Test
    public void testGetBook() {
        Map<String, Object> response = toHashMap(target("books_no_async").path(book1_id).request().get());
        assertNotNull(response);
        assertEquals(response.get("id"), book1_id);
    }

    @Test
    public void testGetBooks() {
        Collection<Map<String, Object>> response = target("books_no_async").request().get(new GenericType<Collection<Map<String, Object>>>() {});
        assertEquals(2, response.size());
    }

    @Test
    public void testAddExtraField() {
        Response response = addBook("author", "title", new Date(), "1111", "hello world");
        assertEquals(200, response.getStatus());

        Map<String, Object> book = toHashMap(response);
        assertNotNull(book.get("id"));
        assertEquals(book.get("extra1"), "hello world");
    }

    @Test
    public void getBooksAsXml() {
        String output = target("books").request(MediaType.APPLICATION_XML).get().readEntity(String.class);
        XML xml = new XMLDocument(output);

        assertEquals("author1", xml.xpath("/books/book[@id='" + book1_id + "']/author/text()").get(0));
        assertEquals("title1", xml.xpath("/books/book[@id='" + book1_id + "']/title/text()").get(0));

        assertEquals(2, xml.xpath("//book/author/text()").size());
    }

    @Test
    public void testAddBookNoAuthor() {
        Response response = addBook(null, "title", new Date(), "1111", "hello world");
        assertEquals(400, response.getStatus());

        String message = response.readEntity(String.class);
        assertTrue(message.contains("author is a required field"));
    }

    @Test
    public void testAddBookNoTitle() {
        Response response = addBook("author", null, new Date(), "1111", "hello world");
        assertEquals(400, response.getStatus());

        String message = response.readEntity(String.class);
        assertTrue(message.contains("title is a required field"));
    }

    @Test
    public void testAddBookNoBook() {
        Response response = target("books_no_async").request().post(null);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void bookNotFoundWithMessage() {
        Response response = target("books_no_async").path("1").request().get();

        assertEquals(404, response.getStatus());

        String message = response.readEntity(String.class);
        assertTrue(message.contains("Book 1 is not found"));
    }

    @Test
    public void testBookEntityTagNotModified() {
        EntityTag entityTag = target("books_no_async").path(book1_id).request().get().getEntityTag();
        assertNotNull(entityTag);

        Response response = target("books_no_async").path(book1_id).request().header("If-None-Match", entityTag).get();
        assertEquals(304, response.getStatus());
    }

    @Test
    public void testUpdatedAuthor() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("author", "updatedAuthor");

        Entity<Map<String, Object>> updateEntity = Entity.entity(updates, MediaType.APPLICATION_JSON);

        Response updateResponse = target("books_no_async").path(book1_id).request().build("PATCH", updateEntity).invoke();

        assertEquals(200, updateResponse.getStatus());

        Response response = target("books_no_async").path(book1_id).request().get();
        Map<String, Object> getResponseMap = toHashMap(response);

        assertEquals("updatedAuthor", getResponseMap.get("author"));

    }

    @Test
    public void testUpdatedExtra() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("hello", "world");

        Entity<Map<String, Object>> updateEntity = Entity.entity(updates, MediaType.APPLICATION_JSON);

        Response updateResponse = target("books_no_async").path(book1_id).request().build("PATCH", updateEntity).invoke();

        assertEquals(200, updateResponse.getStatus());

        Response response = target("books_no_async").path(book1_id).request().get();
        Map<String, Object> getResponseMap = toHashMap(response);

        assertEquals("world", getResponseMap.get("hello"));
    }

    @Test
    public void testUpdateIfMatch() {
        EntityTag entityTag = target("books_no_async").path(book1_id).request().get().getEntityTag();

        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("author", "updatedAuthor");
        Entity<Map<String, Object>> updateEntity = Entity.entity(updates, MediaType.APPLICATION_JSON);
        Response updateResponse = target("books_no_async")
                .path(book1_id)
                .request()
                .header("If-Match", entityTag)
                .build("PATCH", updateEntity).invoke();

        assertEquals(200, updateResponse.getStatus());

        Response updateResponse2 = target("books_no_async")
                .path(book1_id)
                .request()
                .header("If-Match", entityTag)
                .build("PATCH", updateEntity).invoke();

        assertEquals(412, updateResponse2.getStatus());
    }



}
