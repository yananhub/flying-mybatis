package tech.yanand.flyingmybatis.book;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.yanand.flyingmybatis.AutoMapper;

import java.util.Collection;

/**
 * The book mapper
 *
 * @author yanan.zhang
 * @since 2021/4/5
 */
@Mapper
public interface BookMapper extends AutoMapper<Book, Long> {

    Collection<Book> selectByName(@Param("name") String name);

}
