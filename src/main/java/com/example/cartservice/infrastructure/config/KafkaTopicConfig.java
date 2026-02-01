package com.example.cartservice.infrastructure.config;

import com.example.cartservice.infrastructure.stream.CartTopology;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

  @Bean
  public NewTopic cartCommandsTopic() {
    return TopicBuilder.name(CartTopology.COMMANDS_TOPIC)
        .partitions(3)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic cartEventsTopic() {
    return TopicBuilder.name(CartTopology.EVENTS_TOPIC)
        .partitions(3)
        .replicas(1)
        .compact()
        .build();
  }
}
