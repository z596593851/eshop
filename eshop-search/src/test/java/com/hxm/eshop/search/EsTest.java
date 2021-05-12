package com.hxm.eshop.search;

import com.alibaba.fastjson.JSONObject;
import com.hxm.eshop.search.config.EshopEsSearchConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class User{
        private int age;
        private String name;
    }

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest=new IndexRequest("users");
        indexRequest.id("1");
        User user=new User(12,"xiaoming");
        indexRequest.source(JSONObject.toJSONString(user), XContentType.JSON);
        IndexResponse index=client.index(indexRequest, EshopEsSearchConfig.COMMON_OPTIONS);
        System.out.println(index);

    }

    @Test
    public void searchData() throws IOException {
        SearchRequest searchRequest=new SearchRequest();
        //指定索引
        searchRequest.indices("bank");

        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("address","mill"));
        TermsAggregationBuilder ageAgg= AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);

        AvgAggregationBuilder balanceAvg =AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        System.out.println(sourceBuilder.toString());
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse=client.search(searchRequest,EshopEsSearchConfig.COMMON_OPTIONS);
        System.out.println(searchResponse.toString());

        //获取查询数据
        SearchHits hits=searchResponse.getHits();
        SearchHit[] searchHits=hits.getHits();
        for(SearchHit hit:searchHits){
            String s=hit.getSourceAsString();
        }

        //获取聚合信息
        Aggregations aggregations=searchResponse.getAggregations();
        Terms ageAggRes=aggregations.get("ageAgg");
        for(Terms.Bucket bucket:ageAggRes.getBuckets()){
            //年龄
            String keyAsString=bucket.getKeyAsString();
            System.out.println("年龄:"+keyAsString+"=="+bucket.getDocCount());
        }

        Avg balanceAvgRes=aggregations.get("balanceAvg");
        //平均薪资
        double balanceAvgStr=balanceAvgRes.getValue();


//        for(Aggregation aggregation:aggregations.asList()){
//
//        }

    }
}
