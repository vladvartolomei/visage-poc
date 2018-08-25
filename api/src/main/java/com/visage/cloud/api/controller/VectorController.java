package com.visage.cloud.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl;
import com.visage.cloud.api.domain.VectorRepository;
import com.visage.cloud.api.domain.entity.Vector;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.search.DocValueFormat;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController("/vectors")
public class VectorController {

    private static final Logger log = LoggerFactory.getLogger(VectorController.class);
    private HashMap<Long, Double> randomDimensions;

    private RestTemplate rt = new RestTemplate();
    //@Autowired
    //private VectorRepository vectorRepository;

    //@Autowired
    //private ElasticsearchOperations client;

    @GetMapping("/{vectorId}")
    public Optional<Vector> get(@PathVariable String vectorId){
        //return vectorRepository.findById(vectorId);
        return null;
    }

    @GetMapping("/play")
    public List<Vector> play(){
        //return client.queryForList(new NativeSearchQuery(matchAllQuery()), Vector.class);
        /*for (SearchHit hit : sr.getHits()) {
            Beer beer = BeerHelper.toBeer(hit.getSourceAsString());
            Assert.assertEquals("Heineken", beer.getBrand());
            Assert.assertTrue(beer.getPrice()>5 && beer.getPrice()<10);
        }*/
        return null;
    }

    @GetMapping("/trigger-million-inserts")
    public void insertMillion(){
        ExecutorService executor = Executors.newScheduledThreadPool(10);
        IntStream.range(1,100001).forEach(value ->
            {
                executor.submit(() -> {
                    String threadName = Thread.currentThread().getName();
                    log.info("[{} - {}] Execution started", threadName,value);
                    long startTime = System.nanoTime();
                    insertRandomOne(threadName);
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
                    log.info("[{} - {}] Execution finished in {}", threadName, value, duration);
                });
            }
        );
    }

    private void insertRandomOne(String threadName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:9200/vectors-100k/vector");

            HttpEntity<?> entity = new HttpEntity<>(getRandomDimensions(), headers);

            HttpEntity<String> response = rt.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    entity,
                    String.class);
            log.debug("[{}]Received response {}", threadName, response.getBody());
        } catch (HttpClientErrorException e) {
            log.info("[{}]Error occured: {} with body {}", threadName, e.getMessage(), e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            log.info("[{}]Exception occured: {} ", threadName, e.getMessage());
        }
    }

    @GetMapping("/match_all")
    public String searchMatchAll(){

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:9200/_search");

            HttpEntity<?> entity = new HttpEntity<>("{ \"query\": { \"match_all\": {} } }",headers);

            HttpEntity<String> response = rt.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                        String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            log.debug("Received response {}", root);
            return response.getBody();
        }
        catch (Exception e){
            log.info("Error occured: {}",e);
            return null;
        }
    }

    public String getRandomDimensions() throws JsonProcessingException {
        StringJoiner sj = new StringJoiner(",","{","}");
        IntStream.range(1,257).forEach(operand ->{
            Random r = new Random();
            double randomValue = -1 + 2 * r.nextDouble();
            sj.add("\"D"+operand+"\": "+ Double.toString(randomValue));
            }
        );
        sj.add("\"texter\":\""+RandomStringUtils.randomAlphabetic(100)+"\"");
        sj.add("\"created\":"+ System.currentTimeMillis());

        return sj.toString();

    }
}
