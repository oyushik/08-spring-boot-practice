package com.rookies3.myspringboot.controller;

import com.rookies3.myspringboot.entity.Book;
import com.rookies3.myspringboot.exception.BusinessException;
import com.rookies3.myspringboot.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository bookRepository;

    private Book getExistBook(Optional<Book> optionalBook) {
        Book book = optionalBook.orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
        return book;
    }

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @GetMapping
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return optionalBook.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        return optionalBook.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        Book existBook = getExistBook(bookRepository.findById(id));
        existBook.setTitle(bookDetail.getTitle());
        Book updatedBook = bookRepository.save(existBook);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        Book existBook = getExistBook(bookRepository.findById(id));
        bookRepository.delete(existBook);
        return ResponseEntity.ok("Book Deleted");
    }
}
