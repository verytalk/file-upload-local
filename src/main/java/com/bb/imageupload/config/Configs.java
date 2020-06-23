package com.bb.imageupload.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Jason
 */
@Component
@Data
@Configuration
public class Configs {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.type}")
    private String uploadType;

    @Value("${upload.max-size}")
    private int maxSize;

    @Value("${upload.token}")
    private String token;

}
