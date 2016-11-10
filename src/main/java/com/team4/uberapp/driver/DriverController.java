package com.team4.uberapp.driver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.MongoConfiguration;
import org.mongolink.MongoSession;
import com.team4.uberapp.persistence.MongoRepositories;
import spark.Route;
import com.team4.uberapp.util.JsonUtil;

import java.util.List;
import java.util.UUID;

/**
 * Created by HectorGuo on 11/8/16.
 */
public class DriverController extends JsonUtil {
    public static Route getAll = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        List<Driver> drivers = Repositories.drivers().all();

        session.stop();
        res.status(200);
        res.type("application/json");
        return dataToJson(drivers);
    };

    public static Route getById = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        UUID uid = UUID.fromString(req.params(":id"));
        Driver driver = Repositories.drivers().get(uid);

        session.stop();
        res.status(200);
        res.type("application/json");
        return dataToJson(driver);
    };

    public static Route create = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        try{
            ObjectMapper mapper = new ObjectMapper();
            Driver driver = mapper.readValue(req.body(), Driver.class);

            try {
                driver.isValid();
            } catch (Exception e){
                res.status(400);
                return dataToJson(e.getMessage());
            }

            driver.setId(UUID.randomUUID());
            Repositories.drivers().add(driver);

            session.stop();
            res.status(201);
            res.type("application/json");
            return dataToJson(driver);

        }catch (JsonParseException e){
            session.stop();
            res.status(400);
            res.type("application/json");
            return dataToJson(e.getMessage());
        }
    };

    public static Route update = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        UUID uid = UUID.fromString(req.params(":id"));
        Driver driver = Repositories.drivers().get(uid);
        Driver validationDriver = (Driver) driver.clone();

        try{
            ObjectMapper mapper = new ObjectMapper();
            Driver updatedDriver = mapper.readValue(req.body(), Driver.class);

            if(updatedDriver.getName() != null){
                if(!updatedDriver.getName().isEmpty()){
                    validationDriver.setName(updatedDriver.getName());
                }
            }


            try{
                validationDriver.isValid();
            }catch (Exception e){
                session.stop();
                res.status(400);
                return dataToJson(e.getMessage());
            }

            driver.setName(validationDriver.getName());
            session.stop();
            res.status(200);
            res.type("application/json");
            return dataToJson("Driver Updated");

        }catch (JsonParseException e){
            session.stop();
            res.status(400);
            res.type("application/json");
            return dataToJson(e.getMessage());
        }
    };

    public static Route delById = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        UUID uid = UUID.fromString(req.params(":id"));
        Driver driver = Repositories.drivers().get(uid);
        Repositories.drivers().delete(driver);

        session.stop();
        res.status(200);
        res.type("application/json");
        return dataToJson("Driver Deleted");
    };
}
