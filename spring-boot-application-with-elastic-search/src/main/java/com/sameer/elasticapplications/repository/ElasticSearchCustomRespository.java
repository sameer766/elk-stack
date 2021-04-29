package com.sameer.elasticapplications.repository;

import com.sameer.elasticapplications.model.Customer;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSearchCustomRespository extends ElasticsearchRepository<Customer, String> {
  List<Customer> findByFirstName(String fname);
}
