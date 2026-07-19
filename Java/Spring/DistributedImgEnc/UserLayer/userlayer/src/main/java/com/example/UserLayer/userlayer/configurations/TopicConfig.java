package com.example.UserLayer.userlayer.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

  @Bean
  public NewTopic defaultTopic() {
    return new NewTopic("pictures", 1, (short) 1);
  }
}
