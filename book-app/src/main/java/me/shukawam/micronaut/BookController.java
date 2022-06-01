package me.shukawam.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;


import java.util.ArrayList;
import java.util.List;

/**
 * @author shukawam
 */
@Controller("book")
public class BookController {
    private List<Book> bookList = new ArrayList<>();

    public BookController() {
        bookList.add(new Book(1, "Chaos Engineering"));
        bookList.add(new Book(2, "Apache Kafka"));
        bookList.add(new Book(3, "Istio in Action"));
    }

    @Get
    public List<Book> listBooks() {
        return bookList;
    }

    @Get("{/id}")
    public Book getBook(@PathVariable int id) {
        return bookList.get(id);
    }
}
