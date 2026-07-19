package com.example.ServiceLayer.servicelayer.components;

import java.nio.charset.StandardCharsets;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.example.ServiceLayer.servicelayer.models.PictureDoneEvent;
import com.example.ServiceLayer.servicelayer.models.PictureEvent;
import com.example.ServiceLayer.servicelayer.models.Status;
import com.example.ServiceLayer.servicelayer.services.PictureDoneSend;
import com.example.ServiceLayer.servicelayer.services.ProcessRunningServ;
import com.example.ServiceLayer.servicelayer.services.FileService;

@Component
public class PictureProcessingListener {

  PictureDoneSend pictureDoneSend;
  ProcessRunningServ processRunningServ;
  FileService fileService;

  public PictureProcessingListener(PictureDoneSend pictureDoneSend, ProcessRunningServ processRunningServ,
      FileService fileService) {
    this.pictureDoneSend = pictureDoneSend;
    this.processRunningServ = processRunningServ;
    this.fileService = fileService;
  }

  @KafkaListener(topics = "pictures")
  public void handle(PictureEvent event) {
    byte[] asciiKey = event.getAsciiKey();
    PictureEvent.OPPERATION opperation = event.getOpperation();
    String[] command = { fileService.getAbsPathFile(event), new String(asciiKey, StandardCharsets.US_ASCII),
        opperation.getLabel() };
    int exitCode = processRunningServ.run(command);
    if (exitCode != 0) {
      throw new RuntimeException();
    }
    PictureDoneEvent pictureDoneEvent = new PictureDoneEvent();
    StringBuffer sb = new StringBuffer();
    int indexOfDot = event.getName().indexOf('.');
    String baseName = event.getName().substring(0, indexOfDot);
    switch (opperation) {
      case ENCRYPTION:
        sb.append(baseName + ".enc");
        break;
      case DECRYPTION:
        sb.append(baseName + ".dec");
        break;
      default:
        throw new RuntimeException();
    }
    pictureDoneEvent.setId(event.getId());
    pictureDoneEvent.setName(sb.toString());
    pictureDoneEvent.setError(String.valueOf(exitCode));
    pictureDoneEvent.setStatus(Status.COMPLETED);
    pictureDoneSend.send(pictureDoneEvent);
  }
}
