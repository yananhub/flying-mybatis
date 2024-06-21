**Read this in other languages: [English](README.md), [中文](README_zh.md).**

# Flying Mybatis

Let Mybatis fly, provide some basic CRUD methods by simply inheriting `AutoMapper`, without losing any of the original
Mybatis Spring functionally.

Use steps:

Add the jar package dependency, using gradle as an example:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter:x.y.z'
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.y.z'
    implementation 'tech.yanand:flying-mybatis:x.y.z'   // Flying Mybatis jar
}
```

Add configuration in `application.properties` to turn on the camel case conversion, which is required:

```properties
mybatis.configuration.map-underscore-to-camel-case=true
# XML mapper can be turned on if needed, but it is not necessary
mybatis.mapper-locations=classpath:mapper/*.xml
```

Add a Spring configuration class to your project:

```java
@Configuration
@Import(AutoMapperProcessor.class)
class AutoMapperConfig {
}
```

Create a new entity class that maps the tables and columns of the DB using `@Table` and `@Column` annotations.
Take the `Book` entity as an example:

```java
public class Book {

    @Column
    @PrimaryKey
    private Long id;

    @Column
    private String name;

    @Column("release_date")
    private LocalDate publishDate;
}
```

Define the `BookMapper` interface, which extends from the `AutoMapper` interface.
It will derive basic methods of adding, deleting, modifying, selecting, and their functionality.

```java
@Mapper
public interface BookMapper extends AutoMapper<Book, Long> {

    // Custom methods that define SQL in XML Mapper.
    Collection<Book> selectByName(@Param("name") String name);
}
```

Use the `BookMapper`:

```java
@Autowired
private BookMapper bookMapper;

void testBookMapper() {
    int result =                bookMapper.insertAll(List.of(book1, book2));
    int result =                bookMapper.insert(book3);
    
    Collection<Book> bookList = bookMapper.selectAll();
    Book book =                 bookMapper.selectById(1L);
    Collection<Book> bookList = bookMapper.selectAllById(List.of(1L, 0L));
    Collection<Book> bookList = bookMapper.selectAllByColumn("name", "test_book_2");
    long count =                bookMapper.countAll();

    int result =                bookMapper.updateAll(List.of(book3, book4));
    int result =                bookMapper.updateAllSelective(List.of(book3, book4));
    int result =                bookMapper.update(book3);
    int result =                bookMapper.updateSelective(book3);

    int result =                bookMapper.deleteById(1L);
    int result =                bookMapper.deleteAllById(List.of(0L, 1L));
                                bookMapper.deleteAll();
}
```