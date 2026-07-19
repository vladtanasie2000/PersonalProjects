package com.example.ServiceLayer.servicelayer.services;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.ServiceLayer.servicelayer.models.PictureDoneEvent;

@Service
public class PictureDoneSend {
  KafkaTemplate<String, PictureDoneEvent> kafkaTemplate;

  public PictureDoneSend(KafkaTemplate<String, PictureDoneEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send(PictureDoneEvent pictureDoneEvent) {
    kafkaTemplate.send("picturesDone", pictureDoneEvent.getId(), pictureDoneEvent);
  }
}
