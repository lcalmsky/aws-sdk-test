package com.mydata.awssdktest.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SqsTest {

    @Value("${test.sqs.queue-url}")
    private String queueUrl;

    @Test
    void receiveTest() {
        AmazonSQS amazonSqs = AmazonSQSClientBuilder.defaultClient();
        ReceiveMessageResult receiveMessageResult = amazonSqs.receiveMessage(queueUrl);
        System.out.println(receiveMessageResult);
        amazonSqs.deleteMessage(queueUrl, receiveMessageResult.getMessages().get(0).getReceiptHandle());
    }
}