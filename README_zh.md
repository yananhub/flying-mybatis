**其他语言版本: [English](README.md), [中文](README_zh.md).**

# 福来 Mybatis

让 Mybatis 飞起来，通过简单继承 AutoMapper 提供一些基本的增删改查方法，同时不失去任何原始的 Mybatis Spring 功能。

使用步骤：

添加 jar 包依赖，以 gradle 为例：

```groovy
dependencies {
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.y.z'
    implementation 'tech.yanand:flying-mybatis:x.y.z'   // 福来 Mybatis jar
}
```

在 `application.properties` 中增加配置，以开启驼峰命名转换功能，这是必须的：

```properties
mybatis.configuration.map-underscore-to-camel-case=true
# 如果需要可以开启 XML mapper，但这不是必须的
mybatis.mapper-locations=classpath:mapper/*.xml
```

在项目中增 Spring 加配置类：

```java
@Configuration
@Import(AutoMapperProcessor.class)
class AutoMapperConfig {
}
```

新建实体类，使用 `@Table` 和 `@Column` 注解映射数据库的表和列。以 `Book` 实体为例：

```java
@Table
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

定义 `BookMapper` 接口，使它继承自 `AutoMapper` 接口, 它将拥有基本的增删改查方法和功能。

```java
@Mapper
public interface BookMapper extends AutoMapper<Book, Long> {

    // 自定义方法，它可以在 XML Mapper 中定义 SQL。
    Collection<Book> selectByName(@Param("name") String name);
}
```

使用 `BookMapper`：

```java
@Autowired
private BookMapper bookMapper;

void testBookMapper() {
    int result =          bookMapper.insertAll(List.of(book1, book2));
    int result =          bookMapper.insert(book3);

    List<Book> bookList = bookMapper.selectAll();
    Book book =           bookMapper.selectById(1L);
    List<Book> bookList = bookMapper.selectAllById(List.of(1L, 0L));
    List<Book> bookList = bookMapper.selectAllByColumn("name", "test_book_2");
    long count =          bookMapper.countAll();

    int result =          bookMapper.updateAll(List.of(book3, book4));
    int result =          bookMapper.updateAllSelective(List.of(book3, book4));
    int result =          bookMapper.update(book3);
    int result =          bookMapper.updateSelective(book3);

    int result =          bookMapper.deleteById(1L);
    int result =          bookMapper.deleteAllById(List.of(0L, 1L));
    bookMapper.deleteAll();
}
```