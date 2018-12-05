package com.csye6225.fall2018.courseservice.datamodels;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

public class SNSClientConnector
{
    private static AmazonSNS snsClient;

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

        snsClient = AmazonSNSClientBuilder.standard().withCredentials(credentialsProvider).withRegion("us-west-2")
                .build();
        System.out.println("SNS Client Created");

    }

    synchronized public static AmazonSNS getClient()
    {
        if (snsClient == null)
        {
            init();
        }
        return snsClient;
    }
}
