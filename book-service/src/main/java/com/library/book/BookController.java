package com.library.book;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    private static final List<Book> BOOKS = List.of(
            new Book(1L, "Clean Code", "Robert C. Martin"),
            new Book(2L, "Domain-Driven Design", "Eric Evans"),
            new Book(3L, "Designing Data-Intensive Applications", "Martin Kleppmann"));

    @GetMapping
    public List<Book> all() {
        return BOOKS;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> byId(@PathVariable Long id) {
        Optional<Book> book = BOOKS.stream().filter(item -> item.id().equals(id)).findFirst();
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "book-service", "status", "UP");
    }

    public record Book(Long id, String title, String author) {
    }
}
