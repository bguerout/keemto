package fr.keemto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:/META-INF/spring/scheduling-config.xml")
public class SchedulingConfig {
}
