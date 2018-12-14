package com.csye6225.fall2018.courseservice.services;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.DynamoDBConnector;
import com.csye6225.fall2018.courseservice.datamodels.Registrar;

public class RegistrarsService
{
    final private DynamoDBMapper dynamoDBMapper;

    public RegistrarsService()
    {
        dynamoDBMapper = new DynamoDBMapper(DynamoDBConnector.getClient());
    }

    public List<Registrar> getAllRegistrars()
    {
        return dynamoDBMapper.scan(Registrar.class, new DynamoDBScanExpression());
    }

    public Registrar addregistrar(final Registrar registrar)
    {
        if (!verifyRegistrar(registrar) || getRegistrar(registrar.getRegistrarId()) != null)
        {
            return null;
        }
        dynamoDBMapper.save(registrar);
        return getRegistrar(registrar.getRegistrarId());
    }

    public Registrar getRegistrar(final String registrarId)
    {
        final List<Registrar> registrars = dynamoDBMapper.query(Registrar.class,
                UtilsService.<Registrar> composeQueryExpression("registrarId", registrarId));
        if (registrars == null || registrars.isEmpty())
        {
            return null;
        }
        return registrars.get(0);
    }

    public Registrar updateRegistrar(final String registrarId, final Registrar registrar)
    {
        final Registrar oldRegistrar = getRegistrar(registrarId);
        if (oldRegistrar == null || !verifyRegistrar(registrar))
        {
            return null;
        }
        registrar.setId(oldRegistrar.getId());
        registrar.setRegistrarId(oldRegistrar.getRegistrarId());
        dynamoDBMapper.save(registrar);
        return getRegistrar(registrarId);
    }

    public Registrar deleteRegistrar(final String registrarId)
    {
        final Registrar oldRegistrar = getRegistrar(registrarId);
        if (oldRegistrar == null)
        {
            return null;
        }
        dynamoDBMapper.delete(oldRegistrar);
        return oldRegistrar;
    }

    private boolean verifyRegistrar(final Registrar registrar)
    {
        registrar.setId(null);
        if (registrar.getRegistrarId() == null)
        {
            return false;
        }
        if (registrar.getOfferingId() != null && !registrar.getOfferingId().isEmpty()
                && dynamoDBMapper.count(Course.class,
                        UtilsService.<Course> composeQueryExpression("courseId", registrar.getOfferingId())) != 1)
        {
            return false;
        }
        return true;
    }

}
