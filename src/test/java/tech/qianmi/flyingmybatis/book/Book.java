package tech.qianmi.flyingmybatis.book;

import tech.qianmi.flyingmybatis.Column;
import tech.qianmi.flyingmybatis.PrimaryKey;

import java.time.LocalDate;

/**
 * The Book Entity
 *
 * @author yanan.zhang
 * @since 2021/4/5
 */
public class Book {

    @Column
    @PrimaryKey
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDate publishDate;

    public Book() { }

    public Book(String name, LocalDate publishDate) {
        this.name = name;
        this.publishDate = publishDate;
    }

    public Book(Long id, String name, LocalDate publishDate) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }
}
