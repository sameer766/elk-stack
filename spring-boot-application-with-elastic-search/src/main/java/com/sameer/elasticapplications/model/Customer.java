package com.sameer.elasticapplications.model;

import static com.sameer.elasticapplications.util.ApplicationConstant.INDEX;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(
    indexName = INDEX,
    shards = 2
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
  @Id
  private int id;
  private String firstName;
  private String lastName;
  private int age;
  private int money;
}
