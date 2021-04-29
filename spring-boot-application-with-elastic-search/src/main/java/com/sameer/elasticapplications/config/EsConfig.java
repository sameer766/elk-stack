package com.sameer.elasticapplications.config;

import java.io.IOException;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.sameer.elasticapplications.repository")
public class EsConfig {


  public EsConfig() throws IOException {
  }

  public RestHighLevelClient restHighLevelClient() {
    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo("localhost:9200")
        .build();
    RestHighLevelClient client = RestClients.create(clientConfiguration).rest();
    return client;
  }

}
