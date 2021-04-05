package tech.qianmi.flyingmybatis;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import tech.qianmi.flyingmybatis.automapper.BaseMapperProvider;

import java.util.List;

/**
 * The base mapper, inherit this interface to get some basic CRUD methods.
 * @param <T>
 * @param <ID>
 * @author yanan.zhang
 * @since 2021/2/18
 */
public interface BaseMapper<T, ID> {

    @InsertProvider(BaseMapperProvider.class)
    int insert(@Param("entity") T entity);

    @InsertProvider(BaseMapperProvider.class)
    int insertAll(@Param("entities") List<T> entities);

    @SelectProvider(BaseMapperProvider.class)
    T selectById(@Param("id") ID id);

    @SelectProvider(BaseMapperProvider.class)
    List<T> selectAllById(@Param("ids") List<ID> ids);

    @SelectProvider(BaseMapperProvider.class)
    List<T> selectAllByColumn(@Param("column") String column, @Param("value") Object value);

    @SelectProvider(BaseMapperProvider.class)
    List<T> selectAll();

    @SelectProvider(BaseMapperProvider.class)
    long countAll();

    @DeleteProvider(BaseMapperProvider.class)
    int deleteById(@Param("id") ID id);

    @DeleteProvider(BaseMapperProvider.class)
    int deleteAllById(@Param("ids") List<ID> ids);

    @DeleteProvider(BaseMapperProvider.class)
    void deleteAll();

    @UpdateProvider(BaseMapperProvider.class)
    int update(@Param("entity") T entity);

    @UpdateProvider(BaseMapperProvider.class)
    int updateAll(@Param("entities") List<T> entities);

    @UpdateProvider(BaseMapperProvider.class)
    int updateSelective(@Param("entity") T entity);

    @UpdateProvider(BaseMapperProvider.class)
    int updateAllSelective(@Param("entities") List<T> entities);

}
