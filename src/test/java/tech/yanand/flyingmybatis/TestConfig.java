package tech.yanand.flyingmybatis;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
@Import(AutoMapperProcessor.class)
class TestConfig {
}
