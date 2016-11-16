package com.team4.uberapp.ride;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.UberAppUtil;
import org.mongolink.MongoSession;
import spark.Route;

import java.util.List;
import java.util.UUID;

/**
 * Created by HectorGuo on 11/8/16.
 */
public class RideController extends UberAppUtil {
    public static Route getAll = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        List<Ride> rides = Repositories.rides().all();

        session.stop();
        res.status(200);
        res.type("application/json");
        return dataToJson(rides);
    };

    public static Route getById = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        UUID uid = UUID.fromString(req.params(":id"));
        Ride ride = Repositories.rides().get(uid);

        session.stop();
        res.status(200);
        res.type("application/json");
        return dataToJson(ride);
    };

    public static Route create = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        try{
            ObjectMapper mapper = new ObjectMapper();
            Ride ride = mapper.readValue(req.body(), Ride.class);

            try {
                ride.isValid();
            } catch (Exception e){
                res.status(400);
                return dataToJson(e.getMessage());
            }

            ride.setId(UUID.randomUUID());
            Repositories.rides().add(ride);

            session.stop();
            res.status(201);
            res.type("application/json");
            return dataToJson(ride);

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
        Ride ride = Repositories.rides().get(uid);
        Ride validationRide = (Ride) ride.clone();

        try{
            ObjectMapper mapper = new ObjectMapper();
            Ride updatedRide = mapper.readValue(req.body(), Ride.class);

            if(updatedRide.getName() != null){
                if(!updatedRide.getName().isEmpty()){
                    validationRide.setName(updatedRide.getName());
                }
            }


            try{
                validationRide.isValid();
            }catch (Exception e){
                session.stop();
                res.status(400);
                return dataToJson(e.getMessage());
            }

            ride.setName(validationRide.getName());
            session.stop();
            res.status(200);
            res.type("application/json");
            return dataToJson("Ride Updated");

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
        Ride ride = Repositories.rides().get(uid);
        Repositories.rides().delete(ride);

        session.stop();
        res.status(200);
        res.type("application/json");
        return dataToJson("Ride Deleted");
    };
}
