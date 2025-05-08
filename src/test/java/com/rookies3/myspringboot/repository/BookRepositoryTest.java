package com.rookies3.myspringboot.repository;

import com.rookies3.myspringboot.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager; // DB 상태를 직접 관리하거나 확인하기 위해 사용 (optional)

    private final Book book1 = Book.builder()
            .title("스프링 부트 입문")
            .author("홍길동")
            .isbn("9788956746425")
            .price(30000)
            .publishDate(LocalDate.of(2025, 5, 7))
            .build();

    private final Book book2 = Book.builder()
            .title("JPA 프로그래밍")
            .author("박둘리")
            .isbn("9788956746432")
            .price(35000)
            .publishDate(LocalDate.of(2025, 4, 30))
            .build();

    private final Book book3 = Book.builder() // 저자명 검색 테스트를 위해 추가
            .title("또 다른 홍길동 책")
            .author("홍길동")
            .isbn("9788956746449")
            .price(25000)
            .publishDate(LocalDate.of(2025, 6, 1))
            .build();

    /**
     * 도서 등록 테스트
     */
    @Test
    void testCreateBook() {
        // Given: Book 객체 준비 (book1 사용)

        // When: Book 저장
        Book savedBook = bookRepository.save(book1);

        // Then: 저장된 Book 확인
        assertNotNull(savedBook); // 저장된 객체가 null이 아닌지 확인
        assertNotNull(savedBook.getId()); // ID가 생성되었는지 확인
        assertEquals(book1.getTitle(), savedBook.getTitle());
        assertEquals(book1.getAuthor(), savedBook.getAuthor());
        assertEquals(book1.getIsbn(), savedBook.getIsbn());
        assertEquals(book1.getPrice(), savedBook.getPrice());
        assertEquals(book1.getPublishDate(), savedBook.getPublishDate());

        // Optional: DB에서 다시 조회하여 확인
        Optional<Book> foundBookOptional = bookRepository.findById(savedBook.getId());
        assertTrue(foundBookOptional.isPresent());
        Book foundBook = foundBookOptional.get();
        assertEquals(savedBook.getId(), foundBook.getId());
    }

    /**
     * ISBN으로 도서 조회 테스트
     */
    @Test
    void testFindByIsbn() {
        // Given: Book 저장
        entityManager.persist(book1); // TestEntityManager를 사용하여 직접 영속화
        entityManager.flush(); // DB에 반영

        // When: ISBN으로 조회
        Optional<Book> foundBook = bookRepository.findByIsbn(book1.getIsbn());

        // Then: 조회된 Book 확인
        assertNotNull(foundBook); // 조회된 객체가 null이 아닌지 확인
        assertEquals(book1.getTitle(), foundBook.get().getTitle());
        assertEquals(book1.getAuthor(), foundBook.get().getAuthor());
        assertEquals(book1.getIsbn(), foundBook.get().getIsbn());
        assertEquals(book1.getPrice(), foundBook.get().getPrice());
        assertEquals(book1.getPublishDate(), foundBook.get().getPublishDate());
    }

    /**
     * 저자명으로 도서 목록 조회 테스트
     */
    @Test
    void testFindByAuthor() {
        // Given: 동일 저자의 책 두 권 저장
        entityManager.persist(book1); // 저자: 홍길동
        entityManager.persist(book3); // 저자: 홍길동
        entityManager.persist(book2); // 저자: 박둘리 (검색 결과에 포함되지 않아야 함)
        entityManager.flush();

        // When: 저자명으로 조회
        List<Book> booksByAuthor = bookRepository.findByAuthor("홍길동");

        // Then: 조회된 목록 확인
        assertNotNull(booksByAuthor); // 목록이 null이 아닌지 확인
        assertEquals(2, booksByAuthor.size()); // 두 권이 조회되어야 함

        // 조회된 책들이 예상한 책인지 확인 (예: ISBN으로 비교)
        assertTrue(booksByAuthor.stream().anyMatch(book -> book.getIsbn().equals(book1.getIsbn())));
        assertTrue(booksByAuthor.stream().anyMatch(book -> book.getIsbn().equals(book3.getIsbn())));
        assertFalse(booksByAuthor.stream().anyMatch(book -> book.getIsbn().equals(book2.getIsbn())));
    }

    /**
     * 도서 정보 수정 테스트
     */
    @Test
    void testUpdateBook() {
        // Given: Book 저장
        Book savedBook = bookRepository.save(book1);
        entityManager.flush(); // DB에 반영

        // When: Book 정보 수정 후 저장
        String newTitle = "스프링 부트 고급";
        int newPrice = 40000;
        savedBook.setTitle(newTitle);
        savedBook.setPrice(newPrice);

        Book updatedBook = bookRepository.save(savedBook);
        entityManager.flush(); // DB에 반영

        // Then: 수정된 정보 확인
        assertNotNull(updatedBook);
        assertEquals(savedBook.getId(), updatedBook.getId()); // ID는 동일해야 함
        assertEquals(newTitle, updatedBook.getTitle()); // 제목이 수정되었는지 확인
        assertEquals(newPrice, updatedBook.getPrice()); // 가격이 수정되었는지 확인
        assertEquals(book1.getAuthor(), updatedBook.getAuthor()); // 나머지 정보는 그대로인지 확인
    }

    /**
     * 도서 삭제 테스트
     */
    @Test
    void testDeleteBook() {
        // Given: Book 저장
        Book savedBook = bookRepository.save(book1);
        entityManager.flush(); // DB에 반영
        Long bookId = savedBook.getId();

        // When: Book 삭제
        bookRepository.deleteById(bookId);
        entityManager.flush(); // DB에 반영 (즉시 삭제되도록)

        // Then: 삭제 후 조회 시 존재하지 않음 확인
        Optional<Book> deletedBookOptional = bookRepository.findById(bookId);
        assertFalse(deletedBookOptional.isPresent()); // 삭제되었으므로 존재하지 않아야 함
    }
}