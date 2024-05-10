package tech.qianmi.flyingmybatis.automapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.qianmi.flyingmybatis.Column;
import tech.qianmi.flyingmybatis.PrimaryKey;
import tech.qianmi.flyingmybatis.Table;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


/**
 * The meta data cache for DB table information, used for dynamic SQL mapper generation
 *
 * @author yanan.zhang
 * @since 2021/2/18
 */
final class MetaDataCache {

    private static final Logger LOG = LoggerFactory.getLogger(MetaDataCache.class);

    private static final ConcurrentMap<Class<?>, TableInfo> DATA_CACHE = new ConcurrentHashMap<>();

    static final String ENTITY_PLACEHOLDER = "@EP@";

    private MetaDataCache() {
        // Instantiation is not allowed
    }

    static TableInfo getTableInfo(Class<?> mapperType) {
        return DATA_CACHE.computeIfAbsent(mapperType, MetaDataCache::getTableInfoFromType);
    }

    private static TableInfo getTableInfoFromType(Class<?> mapperType) {
        Class<?> beanClass = getBeanType(mapperType);

        TableInfo tableInfo = new TableInfo();
        tableInfo.setDomainName(beanClass.getTypeName());

        LOG.info("Resolved Bean from mapper namespace: [{}]", beanClass.getTypeName());

        Table table = beanClass.getDeclaredAnnotation(Table.class);
        tableInfo.setTableName(isNull(table) ? CaseFormatUtils.toTableName(beanClass.getSimpleName()) : table.value());

        List<ColumnInfo> columnInfos = new ArrayList<>();
        List<String> baseColumns = new ArrayList<>();
        List<String> intoValues = new ArrayList<>();

        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getDeclaredAnnotation(Column.class);
            ColumnInfo columnInfo = new ColumnInfo();
            if (nonNull(column)) {
                columnInfo.setFieldName(field.getName());
                String columnName = column.value();
                columnInfo.setColumnName(columnName.isEmpty() ? CaseFormatUtils.toColumnName(field.getName()) : columnName);
                columnInfo.setString(field.getType().isAssignableFrom(String.class));

                baseColumns.add(columnInfo.getColumnName());
                intoValues.add(String.format("#{%s.%s}", ENTITY_PLACEHOLDER, columnInfo.getFieldName()));

                columnInfos.add(columnInfo);
            }

            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if (nonNull(primaryKey)) {
                if (isNull(column)) {
                    throw new IllegalStateException("Primary key [" + field.getName() + "] must be a column.");
                }
                tableInfo.setPrimaryKey(columnInfo.getColumnName());
                tableInfo.setPrimaryKeyField(columnInfo.getFieldName());
                tableInfo.setKeyType(primaryKey.keyType());
            }
        }

        if (isNull(tableInfo.getPrimaryKey())) {
            throw new IllegalStateException("Table [" + tableInfo.getTableName() + "] must has a primary key.");
        }

        if (tableInfo.getKeyType() == PrimaryKey.KeyType.AUTO) {
            baseColumns.remove(tableInfo.getPrimaryKey());
            intoValues.remove(String.format("#{%s.%s}", ENTITY_PLACEHOLDER, tableInfo.getPrimaryKeyField()));
        }

        tableInfo.setBaseColumns(String.join(", ", baseColumns));
        tableInfo.setIntoValues(String.join(", ", intoValues));
        tableInfo.setColumnInfos(columnInfos);

        return tableInfo;
    }

    private static Class<?> getBeanType(Class<?> mapperType) {
        Type[] genericInterfaces = mapperType.getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
        return (Class) parameterizedType.getActualTypeArguments()[0];
    }
}
