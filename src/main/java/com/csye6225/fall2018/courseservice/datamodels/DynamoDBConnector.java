package com.csye6225.fall2018.courseservice.datamodels;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DynamoDBConnector
{
    private static AmazonDynamoDB dynamoDB;

    private static void init()
    {
        AWSCredentialsProvider credentialsProvider = null;
        try
        {
            credentialsProvider = new InstanceProfileCredentialsProvider(false);
            credentialsProvider.getCredentials();
        }
        catch (Exception e)
        {
            credentialsProvider = new ProfileCredentialsProvider();
            credentialsProvider.getCredentials();
        }

        dynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider).withRegion("us-west-2")
                .build();
        System.out.println("Client Created");

    }

    synchronized public static AmazonDynamoDB getClient()
    {
        if (dynamoDB == null)
        {
            init();
        }
        return dynamoDB;
    }

}
