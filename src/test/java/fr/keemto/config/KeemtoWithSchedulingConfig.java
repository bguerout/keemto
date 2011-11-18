package fr.keemto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(CoreConfig.class)
@ImportResource("classpath*:/META-INF/spring/scheduling-config.xml")
public class KeemtoWithSchedulingConfig {
}
