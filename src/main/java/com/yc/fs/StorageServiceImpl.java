package com.yc.fs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {
    private Path root;

    @Override
    public void init() {
        try {
            if (Files.exists(root)) {
                System.out.println("root = " + root);
                System.out.println("存在？ " + Files.exists(root));
                System.out.println("是目錄？ " + Files.isDirectory(root));
                System.out.println("目錄已存在，跳過創建目錄");
                return;
            }
            System.out.println("目錄不存在，開始創建目錄...");
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("創建目錄失敗： " + e.getMessage());
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            checkDirExist();
            System.out.println("開始儲存檔案");
            Files.copy(file.getInputStream(), this.root.resolve(getNewRandomFilename(file.getOriginalFilename())));
            System.out.println("儲存檔案完成");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            UrlResource urlResource = new UrlResource(file.toUri());

            if (!urlResource.exists()) {
                throw new RuntimeException("無法讀取檔案");
            }
            return urlResource;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        //
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(root, 1).filter(path -> path.equals(this.root)).map(root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("無法讀取檔案");
        }
    }

    private void checkDirExist() {
        init();
    }

    private String getNewRandomFilename(String originalFilename) {
        String filename = "";
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFilename);
        return uuid + "." + extension;
    }

    private String getFileExtension(String filename) {
        int pos = filename.lastIndexOf(".");
        return filename.substring(pos + 1);
    }

    public Path getRoot() {
        return root;
    }

    public void setRoot(Path root) {
        this.root = root;
    }
}
