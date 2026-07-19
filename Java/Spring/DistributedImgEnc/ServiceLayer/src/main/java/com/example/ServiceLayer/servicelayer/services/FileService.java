package com.example.ServiceLayer.servicelayer.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.ServiceLayer.servicelayer.models.PictureEvent;

@Service
public class FileService {

  private final Path location;

  public FileService(@Value("${storage-opt}") String locationS) {
    this.location = Paths.get(locationS).toAbsolutePath();
  }

  public String getAbsPathFile(PictureEvent event) {
    return location.resolve(event.getName()).toAbsolutePath().toString();
  }

}
