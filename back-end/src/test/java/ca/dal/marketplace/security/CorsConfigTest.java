package ca.dal.marketplace.security;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.data.rest.webmvc.config.CorsConfigurationAware;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CorsConfig.class})
class CorsConfigTest {

    private CorsRegistry registry;

    @Before
    public void setUp() {
        this.registry = new CorsRegistry();
    }

    @Test
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {

            private CorsConfigurationAware registry;

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedHeaders("*")
                        .allowedHeaders("*");
                Map<String, CorsConfiguration> configs = this.registry.getCorsConfigurations();
                assertEquals(1, configs.size());
                CorsConfiguration config = configs.get("/foo");
                assertEquals(Arrays.asList("http://domain2.com", "http://domain2.com"), config.getAllowedOrigins());
                assertEquals(Arrays.asList("DELETE"), config.getAllowedMethods());
                assertEquals(Arrays.asList("header1", "header2"), config.getAllowedHeaders());
                assertEquals(Arrays.asList("header3", "header4"), config.getExposedHeaders());
            }
        };
    }
}
