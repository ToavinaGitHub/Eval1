package com.eval1.mg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/vendor/**","/img/**")
                .addResourceLocations("classpath:/static/vendor/", "classpath:/static/img/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic());
    }
}
