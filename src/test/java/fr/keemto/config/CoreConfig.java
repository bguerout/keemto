package fr.keemto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:/META-INF/spring/core-config.xml")
public class CoreConfig {}
