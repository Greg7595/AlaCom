package org.example.alacom.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "discount")
public class DiscountConfig {

    private String fixedValue;
    private List<DiscountThreshold> thresholds;

    @Getter
    @Setter
    public static class DiscountThreshold {
        private Integer requiredQuantity;
        private String value;
    }
}
