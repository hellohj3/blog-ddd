package com.portfolio.blog.config.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${env.imageLoadSrc}")
    String loadSrc;
    @Value("${env.imageUploadSrc}")
    String uploadSrc;
    @Value("${env.imageLoadBufferSrc}")
    String loadBufferSrc;
    @Value("${env.imageUploadBufferSrc}")
    String uploadBufferSrc;
    /**
     * Add handlers to serve static resources such as images, js, and, css
     * files from specific locations under web application root, the classpath,
     * and others.
     *
     * @param registry
     * @see ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(loadSrc + "/**").addResourceLocations("file:///" + uploadSrc + "\\");
        registry.addResourceHandler(loadBufferSrc + "/**").addResourceLocations("file:///" + uploadBufferSrc + "\\");
    }

}
