package com.yc.fs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class FileStorageConfig {
    @Value("${file.path}")
    private String path;

    @Bean
    public StorageService storageService() {
        StorageServiceImpl storageService = new StorageServiceImpl();
        storageService.setRoot(Paths.get(path));
        storageService.init();
        return storageService;
    }
}
