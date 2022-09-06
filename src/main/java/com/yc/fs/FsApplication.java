package com.yc.fs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class FsApplication {
    @Autowired
    StorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(FsApplication.class, args);
    }


    /* @PostMapping("/upload")
     public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
         storageService.save(file);


         long size = file.getSize();
         String name = file.getName();
         String contentType = file.getContentType();
         String originalFilename = file.getOriginalFilename();


         System.out.println("size = " + size);
         System.out.println("name = " + name);
         System.out.println("contentType = " + contentType);
         System.out.println("originalFilename = " + originalFilename);

         Map map = new HashMap();

         map.put("size", size);
         map.put("name", name);
         map.put("contentType", contentType);
         map.put("originalFilename", originalFilename);

         return ResponseEntity.ok(map);
     }*/
    @Component
    public static class StringToADtoConverter implements Converter<String, Adto> {

        @Autowired
        private ObjectMapper objectMapper;

        @Override
        @SneakyThrows
        public Adto convert(String source) {
            return objectMapper.readValue(source, Adto.class);
        }
    }


    @PostMapping("/upload")
    public ResponseEntity a(
            @RequestParam("file") MultipartFile[] files,
            @RequestParam("payload") Adto payload
    ) {

        Map map = new HashMap();

        Arrays.stream(files).forEach(file -> {
            System.out.println(file.getName());

            long size = file.getSize();
            String name = file.getName();
            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();

            storageService.save(file);

            map.put("title", payload.getTitle());
            map.put("desc", payload.getDesc());
            map.put("size", size);
            map.put("name", name);
            map.put("contentType", contentType);
            map.put("originalFilename", originalFilename);
        });


        return ResponseEntity.ok(map);
    }


}






