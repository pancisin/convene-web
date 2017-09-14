package com.pancisin.bookster.components.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String storeBinary(String binary, String filename);
    
    String storeText(String text, String filename, String extension);
    
    void store(MultipartFile file);

    boolean isBinary(String context);
    
    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
