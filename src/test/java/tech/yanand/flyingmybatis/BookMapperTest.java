package tech.yanand.flyingmybatis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import tech.yanand.flyingmybatis.book.Book;
import tech.yanand.flyingmybatis.book.BookMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql("/test-create-schema.sql")
@Sql(scripts = "/test-drop-schema.sql", executionPhase = AFTER_TEST_METHOD)
class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    @BeforeEach
    void insertAll() {
        Book book1 = new Book("test_book_1", LocalDate.now());
        Book book2 = new Book("test_book_2", LocalDate.now());

        int result = bookMapper.insertAll(List.of(book1, book2));

        assertEquals(2, result);
        assertEquals(0, book1.getId());
        assertEquals(1, book2.getId());
    }

    @Test
    void insert() {
        Book book3 = new Book("test_book_3", LocalDate.now());

        int result = bookMapper.insert(book3);

        assertEquals(1, result);
        assertEquals(2, book3.getId());
    }

    @Test
    void selectAll() {
        List<Book> bookList = bookMapper.selectAll();

        assertEquals(2, bookList.size());
    }

    @Test
    void selectById() {
        Book book = bookMapper.selectById(1L);

        assertEquals("test_book_2", book.getName());
    }

    @Test
    void selectAllById() {
        List<Book> bookList = bookMapper.selectAllById(List.of(1L, 0L));

        assertEquals(2, bookList.size());
    }

    @Test
    void selectAllByColumn() {
        List<Book> bookList = bookMapper.selectAllByColumn("name", "test_book_2");

        assertEquals(1, bookList.size());
        assertEquals("test_book_2", bookList.iterator().next().getName());
    }

    @Test
    void selectAllByName() {
        Collection<Book> bookList = bookMapper.selectByName("%test_book_2%");

        assertEquals(1, bookList.size());
        assertEquals("test_book_2", bookList.iterator().next().getName());
    }

    @Test
    void countAll() {
        long count = bookMapper.countAll();

        assertEquals(2, count);
    }

    @Test
    void updateAll() {
        Book book3 = new Book(0L, "test_book_3", LocalDate.now());
        Book book4 = new Book(1L, "test_book_4", LocalDate.now());

        int result = bookMapper.updateAll(List.of(book3, book4));

        assertEquals(2, result);
        assertEquals("test_book_3", bookMapper.selectById(0L).getName());
        assertEquals("test_book_4", bookMapper.selectById(1L).getName());
    }

    @Test
    void updateAllSelective() {
        Book book3 = new Book(0L, "test_book_3", LocalDate.now());
        Book book4 = new Book(1L, "test_book_4", LocalDate.now());

        int result = bookMapper.updateAllSelective(List.of(book3, book4));

        assertEquals(2, result);
        assertEquals("test_book_3", bookMapper.selectById(0L).getName());
        assertEquals("test_book_4", bookMapper.selectById(1L).getName());
    }

    @Test
    void update() {
        Book book3 = new Book(0L, "test_book_3", LocalDate.now());

        int result = bookMapper.update(book3);

        assertEquals(1, result);
        assertEquals("test_book_3", bookMapper.selectById(0L).getName());
    }

    @Test
    void updateSelective() {
        Book book3 = new Book(0L, "test_book_3", LocalDate.now());

        int result = bookMapper.updateSelective(book3);

        assertEquals(1, result);
        assertEquals("test_book_3", bookMapper.selectById(0L).getName());
    }

    @Test
    void deleteById() {
        int result = bookMapper.deleteById(1L);

        assertEquals(1, result);
    }

    @Test
    void deleteAllById() {
        int result = bookMapper.deleteAllById(List.of(0L, 1L));

        assertEquals(2, result);
    }

    @Test
    void deleteAll() {
        bookMapper.deleteAll();

        assertEquals(0, bookMapper.countAll());
    }

}
