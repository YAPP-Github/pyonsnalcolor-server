package com.pyonsnalcolor.batch.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Mapper {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
