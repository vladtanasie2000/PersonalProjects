package com.example.UserLayer.userlayer.restcont;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.example.UserLayer.userlayer.models.PictureEvent;
import com.example.UserLayer.userlayer.models.PictureEvent.OPPERATION;
import com.example.UserLayer.userlayer.services.FileService;

import jakarta.validation.constraints.Pattern;

import com.example.UserLayer.userlayer.models.Status;

@RequestMapping("/")
@Controller
@Validated
public class UploadRestCont {

  public UploadRestCont(KafkaTemplate<String, PictureEvent> kafkaTemplate, FileService fileService) {
    this.kafkaTemplate = kafkaTemplate;
    this.fileService = fileService;
  }

  private final KafkaTemplate<String, PictureEvent> kafkaTemplate;
  private final FileService fileService;

  @GetMapping
  public String getUpload() {
    return "upload";
  }

  @PostMapping
  @ResponseBody
  public ResponseEntity<?> getFile(@RequestParam("file") MultipartFile file,
      @RequestParam("operation") OPPERATION operation,
      @Pattern(regexp = "^[0-9A-F]{32}$", message = "asciiKey must contain exactly 32 hexadecimal characters using 0-9 and A-F") @RequestParam("asciiKey") String asciiKey) {

    if (fileService.createLocalStorage() > 0) {
      System.out.println("Folder has been created");
    }
    fileService.saveMultiPartFile(file);
    PictureEvent pictureEvent = new PictureEvent();
    pictureEvent.setId(UUID.randomUUID().toString());
    pictureEvent.setCreatedAt(Instant.now());
    pictureEvent.setName(file.getOriginalFilename());
    pictureEvent.setStatus(Status.IN_PROGRESS);
    pictureEvent.setOpperation(operation);
    pictureEvent.setAsciiKey(asciiKey.getBytes());
    System.out.println(pictureEvent.getAsciiKey());
    kafkaTemplate.sendDefault(pictureEvent.getId(), pictureEvent);
    System.out.println(file.getOriginalFilename());
    return ResponseEntity.accepted().body(
        Map.of(
            "id", pictureEvent.getId(),
            "status", "IN_PROGRESS"));
  }
}
