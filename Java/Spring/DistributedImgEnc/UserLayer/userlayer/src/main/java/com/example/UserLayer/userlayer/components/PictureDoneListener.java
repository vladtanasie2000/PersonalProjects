package com.example.UserLayer.userlayer.components;

import com.example.UserLayer.userlayer.models.PictureDoneEvent;
import com.example.UserLayer.userlayer.models.Status;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PictureDoneListener {

  private final SimpMessageSendingOperations messagingOperations;

  public PictureDoneListener(
      SimpMessageSendingOperations messagingOperations) {
    this.messagingOperations = messagingOperations;
  }

  @KafkaListener(topics = "picturesDone", groupId = "pictures-done-websocket-group")
  public void handle(PictureDoneEvent event) {
    Map<String, Object> notification = new LinkedHashMap<>();

    notification.put("id", event.getId());
    notification.put("name", event.getName());
    notification.put("status", event.getStatus());
    notification.put("error", event.getError());

    if (Status.COMPLETED.equals(event.getStatus())) {
      String encodedName = URLEncoder.encode(
          event.getName(),
          StandardCharsets.UTF_8);

      notification.put(
          "downloadUrl",
          "/pictures/download/" + encodedName);
    }

    messagingOperations.convertAndSend(
        "/topic/picturesDone",
        (Object) notification);
  }
}
