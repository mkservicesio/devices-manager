package gr.esdalab.atlas.devices.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Main Configurations for devices manager.
 */
@Configuration
public class DevicesManagerConfig {

    @Value("${atlas.urn.prefix}")
    @Getter
    private String atlasUrnPrefix;

    @Bean
    public ResourceBundleMessageSource messageSource() {
        final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages/translations");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

}
