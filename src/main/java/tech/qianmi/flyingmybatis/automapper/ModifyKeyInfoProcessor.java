package tech.qianmi.flyingmybatis.automapper;

import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import tech.qianmi.flyingmybatis.PrimaryKey;

/**
 * Process the mapping between the primary key and the entity field
 *
 * @author yanan.zhang
 * @since 2021/2/18
 */
@Configuration
class ModifyKeyInfoProcessor implements BeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyKeyInfoProcessor.class);

    private static final String INSERT = ".insert";

    private static final String INSERT_ALL = ".insertAll";

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MapperFactoryBean) {
            MapperFactoryBean factoryBean = (MapperFactoryBean) bean;

            modifyKeyInfo(factoryBean, INSERT);
            modifyKeyInfo(factoryBean, INSERT_ALL);
        }
        return bean;
    }

    private void modifyKeyInfo(MapperFactoryBean factoryBean, String methodName) {
        Class<?> mapperInterface = factoryBean.getMapperInterface();
        TableInfo tableInfo = MetaDataCache.getTableInfo(mapperInterface);

        MappedStatement mappedStatement = factoryBean.getSqlSession()
                .getConfiguration().getMappedStatement(mapperInterface.getName() + methodName);

        LOG.info("Modifying key information for [{}]", mappedStatement.getId());

        String[] keyProperties = {tableInfo.getPrimaryKeyField()};
        String[] keyColumns = {tableInfo.getPrimaryKey()};
        BaseMapperProvider.setFieldValue(mappedStatement, "keyProperties", keyProperties);
        BaseMapperProvider.setFieldValue(mappedStatement, "keyColumns", keyColumns);

        if (tableInfo.getKeyType() == PrimaryKey.KeyType.AUTO) {
            BaseMapperProvider.setFieldValue(mappedStatement, "keyGenerator", Jdbc3KeyGenerator.INSTANCE);
        }
    }

}
