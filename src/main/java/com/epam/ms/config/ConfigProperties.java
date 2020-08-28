package com.epam.ms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "caloriescalculator")
public class ConfigProperties {
    double weightFactor;
    double heightFactor;
    double ageFactor;
    double deduction;
}
