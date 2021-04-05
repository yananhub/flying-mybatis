package tech.qianmi.flyingmybatis.book;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.qianmi.flyingmybatis.BaseMapper;

import java.util.List;

/**
 * The book mapper
 *
 * @author yanan.zhang
 * @since 2021/4/5
 */
@Mapper
public interface BookMapper extends BaseMapper<Book, Long> {

    List<Book> selectByName(@Param("name") String name);

}
