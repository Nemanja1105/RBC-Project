package org.unibl.etf.mybudgetbackend.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

/**
 * Configures and provides a ModelMapper bean.
 */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        //mapper.getConfiguration().setAmbiguityIgnored(true);
        mapper.getConfiguration().setSkipNullEnabled(true);
        return mapper;
    }
}
