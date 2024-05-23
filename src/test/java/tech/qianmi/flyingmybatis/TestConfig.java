package tech.qianmi.flyingmybatis;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import tech.qianmi.flyingmybatis.automapper.AutoMapperProcessor;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
@Import(AutoMapperProcessor.class)
class TestConfig {
}
