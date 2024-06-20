package tech.yanand.flyingmybatis;

import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

class MybatisHelper {

    private static final Logger LOG = LoggerFactory.getLogger(MybatisHelper.class);

    private MybatisHelper() {
        // Instantiation is not allowed
    }

    static void setMappedStatementKeys(Configuration configuration, Class<?> mapperInterface, String methodName) {
        MappedStatement mappedStatement = configuration.getMappedStatement(mapperInterface.getName() + methodName);

        LOG.info("Set key information for MappedStatement: [{}]", mappedStatement.getId());

        TableInfo tableInfo = MetaDataCache.getTableInfo(mapperInterface);
        String[] keyProperties = {tableInfo.getPrimaryKeyField()};
        String[] keyColumns = {tableInfo.getPrimaryKey()};
        setFieldValue(mappedStatement, "keyProperties", keyProperties);
        setFieldValue(mappedStatement, "keyColumns", keyColumns);

        if (tableInfo.getKeyType() == PrimaryKey.KeyType.AUTO) {
            setFieldValue(mappedStatement, "keyGenerator", Jdbc3KeyGenerator.INSTANCE);
        }
    }

    static void setFieldValue(Object entity, String fieldName, Object fieldValue) {
        Field field = ReflectionUtils.findField(entity.getClass(), fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, entity, fieldValue);
        } else {
            throw new IllegalStateException("Could not set field [" + fieldName + "].");
        }
    }

    static Object getFieldValue(Object entity, String fieldName) {
        Field field = ReflectionUtils.findField(entity.getClass(), fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, entity);
        } else {
            throw new IllegalStateException("Could not get field [" + fieldName + "].");
        }
    }
}
