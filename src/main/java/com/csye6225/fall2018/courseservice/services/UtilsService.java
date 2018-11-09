package com.csye6225.fall2018.courseservice.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class UtilsService
{

    public static <T> DynamoDBQueryExpression<T> composeQueryExpression(final String idxName, final String idxValue)
    {
        final Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(idxValue));

        return new DynamoDBQueryExpression<T>().withIndexName(idxName).withConsistentRead(false)
                .withKeyConditionExpression(idxName + " = :v1").withExpressionAttributeValues(eav);

    }

    public static <T> DynamoDBQueryExpression<T> composeQueryExpression(final String idxName, final String idxValue,
            final String rangeKeyName, final String rangeKeyValue)
    {
        final Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(idxValue));
        eav.put(":v2", new AttributeValue().withS(rangeKeyValue));

        return new DynamoDBQueryExpression<T>().withIndexName(idxName + "-" + rangeKeyName).withConsistentRead(false)
                .withKeyConditionExpression(idxName + " = :v1 and " + rangeKeyName + " = :v2").withExpressionAttributeValues(eav);
    }

    public static DynamoDBScanExpression composeScanExpression(final String idxName, final String idxValue)
    {
        final Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(idxValue));

        return new DynamoDBScanExpression().withConsistentRead(false).withFilterExpression(idxName + " = :v1")
                .withExpressionAttributeValues(eav);
    }

    public static DynamoDBScanExpression composeScanExpression(final String idxName, final Set<String> idxValues)
    {
        if (idxValues.size() == 1)
        {
            return UtilsService.composeScanExpression(idxName, idxValues.iterator().next());
        }
        final Map<String, AttributeValue> eav = new HashMap<>();
        idxValues.forEach(idxValue -> eav.put(":" + idxValue, new AttributeValue().withS(idxValue)));
        return new DynamoDBScanExpression().withConsistentRead(false)
                .withFilterExpression(idxName + " in (:"
                        + idxValues.stream().reduce((idxV1, idxV2) -> idxV1 + ", :" + idxV2).orElse(null) + ")")
                .withExpressionAttributeValues(eav);
    }

}
