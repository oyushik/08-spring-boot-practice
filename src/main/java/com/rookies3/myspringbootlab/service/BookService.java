package com.rookies3.myspringbootlab.service;

import com.rookies3.myspringbootlab.controller.dto.BookDTO;
import com.rookies3.myspringbootlab.entity.Book;
import com.rookies3.myspringbootlab.exception.BusinessException;
import com.rookies3.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    // create
    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        bookRepository.findByIsbn(request.getIsbn())
                .ifPresent(book -> {
                    throw new BusinessException("이미 존재하는 도서입니다.", HttpStatus.CONFLICT);
                });

        Book book = request.toEntity();
        Book savedBook = bookRepository.save(book);
        return new BookDTO.BookResponse(savedBook);
    }

    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(() -> new BusinessException("해당하는 도서가 없습니다.", HttpStatus.NOT_FOUND));
    }

    // update
    @Transactional
    public BookDTO.BookResponse updateBookByIsbn(String isbn, BookDTO.BookUpdateRequest bookDetail) {
        Book book = getBookByIsbn(isbn);
        book.setTitle(bookDetail.getTitle());
        return new BookDTO.BookResponse(book);
    }

    // delete
    @Transactional
    public void deleteBook(String isbn) {
        Book book = getBookByIsbn(isbn);
        bookRepository.delete(book);
    }
}