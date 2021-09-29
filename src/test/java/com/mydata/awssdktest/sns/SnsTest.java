package com.mydata.awssdktest.sns;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SnsTest {

    private static AmazonSNSClient snsClient;
    @Value("${sns.topic-arn}")
    private String topicArn;
    @Value("${sns.protocol}")
    private String protocol;
    @Value("${sns.endpoint}")
    private String endpoint;
    @Value("${sns.subscription-arn}")
    private String subscriptionArn;

    @BeforeAll
    static void beforeAll() {
        snsClient = (AmazonSNSClient) AmazonSNSClient.builder()
                .withRegion(Regions.AP_NORTHEAST_2.getName())
                .build();
    }

    @Test
    @DisplayName("Create a SNS topic")
    void create() {
        CreateTopicResult createTopicResult = snsClient.createTopic("test");
        System.out.println(createTopicResult);
    }

    @Test
    @DisplayName("List SNS topics")
    void listTopics() {
        ListTopicsResult listTopicsResult = snsClient.listTopics();
        List<Topic> topics = listTopicsResult.getTopics();
        for (Topic topic : topics) {
            System.out.println(topic);
        }
    }

    @Test
    @DisplayName("List SNS subscriptions by topic")
    void listSubscriptions() {
        ListSubscriptionsByTopicResult listSubscriptionsByTopicResult = snsClient.listSubscriptionsByTopic(topicArn);
        List<Subscription> subscriptions = listSubscriptionsByTopicResult.getSubscriptions();
        for (Subscription subscription : subscriptions) {
            System.out.println(subscription);
        }
    }

    @Test
    @DisplayName("Subscribe a topic")
    void subscribe() {
        SubscribeResult subscribeResult = snsClient.subscribe(topicArn, protocol, endpoint);
        System.out.println(subscribeResult);
    }

    @Test
    @DisplayName("Unsubscribe a topic")
    void unsubscribe() {
        UnsubscribeResult unsubscribeResult = snsClient.unsubscribe(subscriptionArn);
        System.out.println(unsubscribeResult);
    }
}