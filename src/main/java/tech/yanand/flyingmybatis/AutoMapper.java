package tech.yanand.flyingmybatis;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.Collection;

/**
 * The auto mapper, inherit this interface to get some basic CRUD methods.
 *
 * @param <E> The entity mapping to the DB table
 * @param <K> The key of the table. It can only be a subtype of {@link Number} or {@link CharSequence}
 * @author Richard Zhang
 */
public interface AutoMapper<E, K> {

    /**
     * Insert an entity.
     *
     * @param entity The entity to be inserted.
     * @return Inserted count.
     */
    @InsertProvider(AutoMapperProvider.class)
    int insert(@Param("entity") E entity);

    /**
     * Insert multiple entities
     *
     * @param entities Entities to be inserted.
     * @return Inserted count.
     */
    @InsertProvider(AutoMapperProvider.class)
    int insertAll(@Param("entities") Collection<E> entities);

    /**
     * Select an entity by the ID.
     *
     * @param id ID
     * @return Selected entity.
     */
    @SelectProvider(AutoMapperProvider.class)
    E selectById(@Param("id") K id);

    /**
     * Select entities by the ID collection.
     *
     * @param ids ID collection.
     * @return Selected entities.
     */
    @SelectProvider(AutoMapperProvider.class)
    Collection<E> selectAllById(@Param("ids") Collection<K> ids);

    /**
     * Select entities by a column.
     *
     * @param column Column name.
     * @param value  The value to be selected of the {@code column.}
     * @return Selected entities.
     */
    @SelectProvider(AutoMapperProvider.class)
    Collection<E> selectAllByColumn(@Param("column") String column, @Param("value") Object value);

    /**
     * Select all entities.
     *
     * @return Selected entities.
     */
    @SelectProvider(AutoMapperProvider.class)
    Collection<E> selectAll();

    /**
     * Count all entities.
     *
     * @return All entities count.
     */
    @SelectProvider(AutoMapperProvider.class)
    long countAll();

    /**
     * Delete an entity by ID
     *
     * @param id Entity ID.
     * @return Deleted count.
     */
    @DeleteProvider(AutoMapperProvider.class)
    int deleteById(@Param("id") K id);

    /**
     * Delete entities by the ID collection.
     *
     * @param ids ID collection.
     * @return Deleted count.
     */
    @DeleteProvider(AutoMapperProvider.class)
    int deleteAllById(@Param("ids") Collection<K> ids);

    /**
     * Delete all entities.
     */
    @DeleteProvider(AutoMapperProvider.class)
    void deleteAll();

    /**
     * Update an entity by the ID.
     * <p><b>Note:</b> If fields in the entity are passed {@code null} values,
     * the column values in DB are updated to be {@code null}.
     *
     * @param entity The entity to be updated.
     * @return Updated count.
     */
    @UpdateProvider(AutoMapperProvider.class)
    int update(@Param("entity") E entity);

    /**
     * Update entities by their ID.
     * <p><b>Note:</b> If fields in the entity are passed {@code null} values,
     * the column values in DB are updated to be {@code null}.
     *
     * @param entities Entities to be updated.
     * @return Updated count.
     */
    default int updateAll(Collection<E> entities) {
        return entities.stream().map(this::update).mapToInt(Integer::intValue).sum();
    }

    /**
     * Selectively update an entity by the ID.
     * If a field in entity is passed {@code null} value, it will not be updated.
     *
     * @param entity The entity to be updated.
     * @return Updated count.
     */
    @UpdateProvider(AutoMapperProvider.class)
    int updateSelective(@Param("entity") E entity);

    /**
     * Selectively update entities by their ID.
     * If a field in entity is passed {@code null} value, it will not be updated.
     *
     * @param entities Entities to be updated.
     * @return Updated count.
     */
    default int updateAllSelective(Collection<E> entities) {
        return entities.stream().map(this::updateSelective).mapToInt(Integer::intValue).sum();
    }
}
