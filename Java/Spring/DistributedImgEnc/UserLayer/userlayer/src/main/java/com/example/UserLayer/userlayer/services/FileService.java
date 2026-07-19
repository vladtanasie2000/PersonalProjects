package com.example.UserLayer.userlayer.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

  private final Path storageDirectory;

  public FileService(@Value("${storage-opt}") String storage_op) {
    this.storageDirectory = Paths
        .get(storage_op)
        .toAbsolutePath()
        .normalize();
  }

  public Resource getFileUri(String fileName) {
    Path filePath = storageDirectory.resolve(fileName).normalize();
    try {
      return new UrlResource(filePath.toUri());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public int createLocalStorage() {
    if (!Files.exists(storageDirectory, LinkOption.NOFOLLOW_LINKS)) {
      try {
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        Files.createDirectory(storageDirectory, attr);
      } catch (IOException e) {
        e.printStackTrace();
        return -1;
      }
    }

    return 0;
  }

  public int saveMultiPartFile(MultipartFile multiFile) {
    try {
      multiFile.transferTo(storageDirectory.resolve(multiFile.getOriginalFilename()));
    } catch (IllegalStateException | IOException e) {
      e.printStackTrace();
      return -1;
    }
    return 0;
  }
}
