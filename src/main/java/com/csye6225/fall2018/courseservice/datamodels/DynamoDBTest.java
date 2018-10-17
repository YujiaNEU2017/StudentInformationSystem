package com.csye6225.fall2018.courseservice.datamodels;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

public class DynamoDBTest
{
    static AmazonDynamoDB dynamoDB;

    private static void init() throws Exception
    {
        AWSCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        credentialsProvider.getCredentials();
        dynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider).withRegion("us-west-2")
                .build();
    }

    public static void main(String[] args) throws Exception
    {
        init();
        String tableName = "student-test";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("studentId", new AttributeValue().withS("aaa"));
        GetItemRequest getItemRequest = new GetItemRequest();
        getItemRequest.setKey(item);
        getItemRequest.setTableName(tableName);
        GetItemResult getItemResult = dynamoDB.getItem(getItemRequest);
        System.out.println(getItemResult);
    }
}
