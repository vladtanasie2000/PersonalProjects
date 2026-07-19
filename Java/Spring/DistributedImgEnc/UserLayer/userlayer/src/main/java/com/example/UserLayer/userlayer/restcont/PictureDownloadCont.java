package com.example.UserLayer.userlayer.restcont;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.UserLayer.userlayer.services.FileService;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@RestController
public class PictureDownloadCont {

  private FileService fileService;

  public PictureDownloadCont(FileService fileService) {
    this.fileService = fileService;
  }

  @GetMapping("/pictures/download/{fileName:.+}")
  public ResponseEntity<Resource> download(
      @PathVariable String fileName) throws MalformedURLException {

    Resource resource = fileService.getFileUri(fileName);
    ContentDisposition disposition = ContentDisposition.attachment()
        .filename(
            fileName,
            StandardCharsets.UTF_8)
        .build();

    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            disposition.toString())
        .header(
            HttpHeaders.CONTENT_TYPE,
            "application/octet-stream")
        .body(resource);
  }
}
