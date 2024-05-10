package tech.qianmi.flyingmybatis.automapper;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import tech.qianmi.flyingmybatis.AutoMapper;

import java.util.Arrays;

/**
 * Process the mapping between the primary key and the entity field
 *
 * @author yanan.zhang
 * @since 2021/2/18
 */
public class AutoMapperProcessor implements BeanPostProcessor {

    private static final String INSERT = ".insert";

    private static final String INSERT_ALL = ".insertAll";

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MapperFactoryBean<?> factoryBean) {
            modifyKeyInfo(factoryBean, INSERT);
            modifyKeyInfo(factoryBean, INSERT_ALL);
        }
        return bean;
    }

    private void modifyKeyInfo(MapperFactoryBean<?> factoryBean, String methodName) {
        Class<?> mapperInterface = factoryBean.getMapperInterface();

        if (isAutoMapperBased(mapperInterface))
            MybatisHelper.setMappedStatementKeys(factoryBean.getSqlSession().getConfiguration(),
                    mapperInterface, methodName);
    }

    private boolean isAutoMapperBased(Class<?> mapperInterface) {
        return Arrays.stream(mapperInterface.getInterfaces()).anyMatch(AutoMapper.class::equals);
    }
}
