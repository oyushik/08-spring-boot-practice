package com.rookies3.myspringbootlab.controller.dto;

import com.rookies3.myspringbootlab.entity.Book;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class BookDTO {

    @Getter
    @Setter
    public static class BookCreateRequest {
        @NotBlank(message = "필수 항목입니다.")
        private String title;

        @NotBlank(message = "필수 항목입니다.")
        private String author;

        @NotBlank(message = "필수 항목입니다.")
        private String isbn;

        @NotBlank(message = "필수 항목입니다.")
        private Integer price;

        @NotBlank(message = "필수 항목입니다.")
        private LocalDate publishDate;

        public Book toEntity() {
            Book book = new Book();
            book.setTitle(this.title);
            book.setAuthor(this.author);
            book.setIsbn(this.isbn);
            book.setPrice(this.price);
            book.setPublishDate(this.publishDate);
            return book;
        }
    }

    @Getter
    @Setter
    public static class BookUpdateRequest {
        @NotBlank(message = "필수 항목입니다.")
        private String title;

        @NotBlank(message = "필수 항목입니다.")
        private String author;

        @NotBlank(message = "필수 항목입니다.")
        private String isbn;

        @NotBlank(message = "필수 항목입니다.")
        private Integer price;

        @NotBlank(message = "필수 항목입니다.")
        private LocalDate publishDate;
    }

    @Getter
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;

        public BookResponse(Book book) {
            this.id = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.isbn = book.getIsbn();
            this.price = book.getPrice();
            this.publishDate = book.getPublishDate();
        }
    }
}
