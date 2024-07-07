/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.yanand.flyingmybatis;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Arrays;

/**
 * Process the mapping between the primary key and the entity field
 *
 * @author Richard Zhang
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
