package com.sameer.elasticapplications;

import static com.sameer.elasticapplications.util.ApplicationConstant.INDEX;

import com.alibaba.fastjson.JSON;
import com.sameer.elasticapplications.model.Customer;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchQueryBuilder {
  @Autowired
  RestHighLevelClient highLevelClient;

  public List<Customer> findByText(String text) throws IOException {
    BoolQueryBuilder query = QueryBuilders.boolQuery()
        .should(QueryBuilders.queryStringQuery(text)
                    .lenient(true)
                    .field("firstName"))
         .should(QueryBuilders.queryStringQuery("*"+text+"*").lenient(true).field("firstName"));


     SearchResponse response = highLevelClient.search(new SearchRequest().source(new SearchSourceBuilder()
                                                                                        .size(10)
                                                                                        .query(query))
                                                             .indices("elastic-application"),
                                                         RequestOptions.DEFAULT);
    SearchHit[] searchHits = response.getHits().getHits();
    List<Customer> results =
        Arrays.stream(searchHits)
            .map(hit -> JSON.parseObject(hit.getSourceAsString(), Customer.class))
            .collect(Collectors.toList());
    return results;
  }
  public List<Customer> findByBoolQuery(String text) throws IOException {
    BoolQueryBuilder query = QueryBuilders.boolQuery()
        .should(QueryBuilders.queryStringQuery(text)
                    .lenient(true)
                    .field("firstName"))
        .should(QueryBuilders.queryStringQuery("*"+text+"*").lenient(true).field("firstName"));


    SearchResponse response = highLevelClient.search(new SearchRequest().source(new SearchSourceBuilder()
                                                                                    .size(10)
                                                                                    .query(query))
                                                         .indices("elastic-application"),
                                                     RequestOptions.DEFAULT);
    SearchHit[] searchHits = response.getHits().getHits();
    List<Customer> results =
        Arrays.stream(searchHits)
            .map(hit -> JSON.parseObject(hit.getSourceAsString(), Customer.class))
            .collect(Collectors.toList());
    return results;
  }


  public List<Object> getHistogram() throws IOException {
    SearchRequest searchRequest=new SearchRequest(INDEX);
    searchRequest.source().size(0);
    searchRequest.source().aggregation(AggregationBuilders.histogram("age").interval(5).field("age"));
//                                           .subAggregation(AggregationBuilders.avg("money").field("money")));
     SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println(response.toString());
    Histogram histogram=response.getAggregations().get("age");
//    List<Double> list=histogram.getBuckets().stream().map(bucket -> {
//      Avg avg=bucket.getAggregations().get("money");
//      return avg.getValue();
//    }).collect(Collectors.toList());
    final List<Object> collect = histogram.getBuckets().stream().map(
        MultiBucketsAggregation.Bucket::getKey).collect(Collectors.toList());

    return collect;
  }
}
