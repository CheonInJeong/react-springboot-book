package com.example.book.web;

import com.example.book.domain.Book;
import com.example.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class BookController {

    private final BookService bookService;

    @PostMapping("/book")
    public ResponseEntity<?> save(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.save(book),HttpStatus.CREATED);
    }

    @GetMapping("/book")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Book book) {
        return new ResponseEntity<>(bookService.updateBook(id,book), HttpStatus.OK);
    }
    @DeleteMapping("/book/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }

}
