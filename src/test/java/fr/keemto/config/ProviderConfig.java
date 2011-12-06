package fr.keemto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:/META-INF/spring/provider-config.xml")
public class ProviderConfig {
}
