package com.sameer.elasticapplications;

import static com.sameer.elasticapplications.util.ApplicationConstant.INDEX;

import com.alibaba.fastjson.JSON;
import com.sameer.elasticapplications.config.EsConfig;
import com.sameer.elasticapplications.model.Customer;
import com.sameer.elasticapplications.repository.ElasticSearchCustomRespository;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class ElasticApplication {

	Logger logger=LoggerFactory.getLogger(ElasticApplication.class);
	@Autowired
	private ElasticSearchCustomRespository elasticSearchCustomRespository;
	@Autowired
	private EsConfig esConfig;
	@Autowired
	SearchQueryBuilder searchQueryBuilder;

	@PostMapping("/saveUser")
	public void save(@RequestBody List<Customer> customers)
	{
		logger.info("Saved data for the customers {}",customers);
		elasticSearchCustomRespository.saveAll(customers);
	}
	@GetMapping("/getAllUser")
	public Iterable<Customer> findAll()
	{
		return elasticSearchCustomRespository.findAll();
	}

	@GetMapping("/findByName/{Name}")
	public List<Customer> findById(@PathVariable String Name) throws IOException {
		QueryBuilder queryBuilder= QueryBuilders.multiMatchQuery(Name,"firstName","lastName");
		SearchRequest searchRequest = new SearchRequest(INDEX).source(new SearchSourceBuilder().query(queryBuilder));
		SearchResponse response = esConfig.restHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
		SearchHit[] searchHits = response.getHits().getHits();
		List<Customer> results =
				Arrays.stream(searchHits)
						.map(hit -> JSON.parseObject(hit.getSourceAsString(), Customer.class))
						.collect(Collectors.toList());
		logger.info("Returned result for user {}",results);
		return results;
	}

	@DeleteMapping("/delete")
	public void delete()
	{
		elasticSearchCustomRespository.deleteAll();
	}

	@GetMapping("/findByFirstName/{firstName}")
	public List<Customer> getAll(@PathVariable String firstName) throws IOException {

		return searchQueryBuilder.findByText(firstName);
	}
	@GetMapping("/findByBoolName/{firstName}")
	public List<Customer> findByText(@PathVariable String firstName) throws IOException {

		return searchQueryBuilder.findByBoolQuery(firstName);
	}
	@GetMapping("/histogram")
	public List<Object> getTheHistogram() throws IOException {

		return searchQueryBuilder.getHistogram();
	}



	public static void main(String[] args) {
		SpringApplication.run(ElasticApplication.class, args);
	}

}
