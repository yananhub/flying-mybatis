package tech.qianmi.flyingmybatis.automapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tech.qianmi.flyingmybatis.PrimaryKey.KeyType;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;

/**
 * The core CRUD provider implementation
 *
 * @author yanan.zhang
 * @since 2021/2/18
 */
public class BaseMapperProvider implements ProviderMethodResolver {

    private final static String INSERT_ALL = "<script>\n" +
            "    INSERT INTO %s (%s) VALUES\n" +
            "    <foreach collection=\"entities\" item=\"entity\" separator=\",\">\n" +
            "         (%s)\n" +
            "    </foreach>\n" +
            "</script>";

    private final static String UPDATE_ALL = "<script>\n" +
            "    <foreach collection=\"entities\" item=\"entity\">\n" +
            "         %s;\n" +
            "    </foreach>\n" +
            "</script>";

    private final static String UPDATE_ALL_SELECTIVE = "<script>\n" +
            "    <foreach collection=\"entities\" item=\"entity\">\n" +
            "         update %s <set>%s</set> where %s = #{entity.%s};\n" +
            "    </foreach>\n" +
            "</script>";

    private final static String WHERE_COLUMN_EQUALS = "%s = #{%s}";

    private final static String WHERE_ID_IN = "%s in (%s)";

    private final static String SET_COLUMN = "%s = #{entity.%s}";

    private final static String SET_IF_COLUMN = "<if test=\"entity.%s != null\">%s = #{entity.%s},</if>";

    private final static String TRUNCATE_TABLE = "truncate table %s";

    public static <T> String insert(@Param("entity") T entity, ProviderContext context) {
        entity = getParam(entity, "entity");
        requireNonNull(entity, "Entity is null");

        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        if (tableInfo.getKeyType() == KeyType.UUID)
            setFieldValue(entity, tableInfo.getPrimaryKeyField(), randomUUID().toString());

        return new SQL().INSERT_INTO(tableInfo.getTableName())
                .INTO_COLUMNS(tableInfo.getBaseColumns())
                .INTO_VALUES(tableInfo.getIntoValues())
                .toString();
    }

    public static <T> String insertAll(@Param("entities") Collection<T> entities, ProviderContext context) {
        checkArgument(entities, "Entities is null or empty");

        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        if (tableInfo.getKeyType() == KeyType.UUID)
            entities.forEach(entity -> setFieldValue(entity, tableInfo.getPrimaryKeyField(), randomUUID().toString()));

        return String.format(INSERT_ALL,
                tableInfo.getTableName(),
                tableInfo.getBaseColumns(),
                tableInfo.getIntoValues());
    }

    public static <ID> String selectById(@Param("id") ID id, ProviderContext context) {
        id = getParam(id, "id");
        requireNonNull(id, "ID is null");

        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        return new SQL()
                .SELECT("*")
                .FROM(tableInfo.getTableName())
                .WHERE(String.format(WHERE_COLUMN_EQUALS, tableInfo.getPrimaryKey(), "id"))
                .toString();
    }

    public static <ID> String selectAllById(@Param("ids") List<ID> ids, ProviderContext context) {
        checkArgument(ids, "IDs is null or empty");

        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        return new SQL()
                .SELECT("*")
                .FROM(tableInfo.getTableName())
                .WHERE(String.format(WHERE_ID_IN, tableInfo.getPrimaryKey(), getIdIn(ids.size())))
                .toString();
    }

    public static String selectAllByColumn(@Param("column") String column, @Param("value") Object value,
                                                ProviderContext context) {
        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        return new SQL()
                .SELECT("*")
                .FROM(tableInfo.getTableName())
                .WHERE(String.format(WHERE_COLUMN_EQUALS, column, "value"))
                .toString();
    }

    public static String selectAll(ProviderContext context) {
        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        return new SQL()
                .SELECT("*")
                .FROM(tableInfo.getTableName())
                .toString();
    }

    public static String countAll(ProviderContext context) {
        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        return new SQL()
                .SELECT("count(*)")
                .FROM(tableInfo.getTableName())
                .toString();
    }

    public static <ID> String deleteById(@Param("id") ID id, ProviderContext context) {
        id = getParam(id, "id");
        requireNonNull(id, "ID is null");

        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        return new SQL()
                .DELETE_FROM(tableInfo.getTableName())
                .WHERE(String.format(WHERE_COLUMN_EQUALS, tableInfo.getPrimaryKey(), "id"))
                .toString();
    }

    public static <ID> String deleteAllById(@Param("ids") List<ID> ids, ProviderContext context) {
        checkArgument(ids, "IDs is null or empty");

        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        return new SQL()
                .DELETE_FROM(tableInfo.getTableName())
                .WHERE(String.format(WHERE_ID_IN, tableInfo.getPrimaryKey(), getIdIn(ids.size())))
                .toString();
    }

    public static String deleteAll(ProviderContext context) {
        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());
        return String.format(TRUNCATE_TABLE, tableInfo.getTableName());
    }

    public static <T> String update(@Param("entity") T entity, ProviderContext context) {
        entity = getParam(entity, "entity");
        requireNonNull(entity, "Entity is null");

        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());

        return buildUpdateSql(tableInfo);
    }

    public static <T> String updateAll(@Param("entities") List<T> entities, ProviderContext context) {
        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());

        checkArgument(entities, "Entities is null or empty");
        checkArgument(entities.stream()
                .map(entity -> getFieldValue(entity, tableInfo.getPrimaryKeyField()))
                .noneMatch(Objects::isNull),
                "Updated entities contains nullable ID");

        return String.format(UPDATE_ALL, buildUpdateSql(tableInfo));
    }

    public static <T> String updateSelective(@Param("entity") T entity, ProviderContext context) {
        entity = getParam(entity, "entity");
        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());

        requireNonNull(entity, "Entity is null");
        requireNonNull(getFieldValue(entity, tableInfo.getPrimaryKeyField()),
                "Updated entity ID is null");

        SQL sql = new SQL().UPDATE(tableInfo.getTableName());
        for (ColumnInfo columnInfo : tableInfo.getColumnInfos()) {
            if (nonNull(getFieldValue(entity, columnInfo.getFieldName()))) {
                sql.SET(String.format(SET_COLUMN, columnInfo.getColumnName(), columnInfo.getFieldName()));
            }
        }
        sql.WHERE(String.format(WHERE_COLUMN_EQUALS, tableInfo.getPrimaryKey(), "entity." + tableInfo.getPrimaryKeyField()));
        return sql.toString();
    }

    public static <T> String updateAllSelective(@Param("entities") List<T> entities, ProviderContext context) {
        TableInfo tableInfo = MetaDataCache.getTableInfo(context.getMapperType());

        checkArgument(entities, "Entities is null or empty");
        checkArgument(entities.stream()
                        .map(entity -> getFieldValue(entity, tableInfo.getPrimaryKeyField()))
                        .noneMatch(Objects::isNull),
                "Updated entities contains nullable ID");

        String setIfColumns = tableInfo.getColumnInfos().stream()
                .map(info -> String.format(SET_IF_COLUMN, info.getFieldName(), info.getColumnName(), info.getFieldName()))
                .collect(Collectors.joining("\n"));

        return String.format(UPDATE_ALL_SELECTIVE,
                tableInfo.getTableName(), setIfColumns, tableInfo.getPrimaryKey(), tableInfo.getPrimaryKeyField());
    }

    private static String buildUpdateSql(TableInfo tableInfo) {
        SQL sql = new SQL().UPDATE(tableInfo.getTableName());
        for (ColumnInfo columnInfo : tableInfo.getColumnInfos()) {
            sql.SET(String.format(SET_COLUMN, columnInfo.getColumnName(), columnInfo.getFieldName()));
        }
        sql.WHERE(String.format(WHERE_COLUMN_EQUALS, tableInfo.getPrimaryKey(), "entity." + tableInfo.getPrimaryKeyField()));
        return sql.toString();
    }

    static void setFieldValue(Object entity, String fieldName, Object fieldValue) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String getIdIn(int idSize) {
        return IntStream.range(0, idSize)
                .mapToObj(index -> "#{ids[" + index + "]}")
                .collect(Collectors.joining(", "));
    }

    private static Object getFieldValue(Object entity, String fieldName) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getParam(Object paramMap, String key) {
        return ((Map<String, T>) paramMap).get(key);
    }

    private static <T> void checkArgument(Collection<T> arg, String message) {
        if (arg == null || arg.isEmpty())
            throw new IllegalArgumentException(message);
    }

    private static void checkArgument(boolean valid, String message) {
        if (!valid)
            throw new IllegalArgumentException(message);
    }

}
