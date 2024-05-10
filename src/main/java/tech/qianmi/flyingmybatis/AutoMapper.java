package tech.qianmi.flyingmybatis;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import tech.qianmi.flyingmybatis.automapper.AutoMapperProvider;

import java.util.Collection;

/**
 * The auto mapper, inherit this interface to get some basic CRUD methods.
 *
 * @param <E>
 * @param <K>
 * @author yanan.zhang
 * @since 2021/2/18
 */
public interface AutoMapper<E, K> {

    @InsertProvider(AutoMapperProvider.class)
    int insert(@Param("entity") E entity);

    @InsertProvider(AutoMapperProvider.class)
    int insertAll(@Param("entities") Collection<E> entities);

    @SelectProvider(AutoMapperProvider.class)
    E selectById(@Param("id") K id);

    @SelectProvider(AutoMapperProvider.class)
    Collection<E> selectAllById(@Param("ids") Collection<K> ids);

    @SelectProvider(AutoMapperProvider.class)
    Collection<E> selectAllByColumn(@Param("column") String column, @Param("value") Object value);

    @SelectProvider(AutoMapperProvider.class)
    Collection<E> selectAll();

    @SelectProvider(AutoMapperProvider.class)
    long countAll();

    @DeleteProvider(AutoMapperProvider.class)
    int deleteById(@Param("id") K id);

    @DeleteProvider(AutoMapperProvider.class)
    int deleteAllById(@Param("ids") Collection<K> ids);

    @DeleteProvider(AutoMapperProvider.class)
    void deleteAll();

    @UpdateProvider(AutoMapperProvider.class)
    int update(@Param("entity") E entity);

    default int updateAll(Collection<E> entities) {
        return entities.stream().map(this::update).mapToInt(Integer::intValue).sum();
    }

    @UpdateProvider(AutoMapperProvider.class)
    int updateSelective(@Param("entity") E entity);

    default int updateAllSelective(Collection<E> entities) {
        return entities.stream().map(this::updateSelective).mapToInt(Integer::intValue).sum();
    }
}
