package com.team4.uberapp.ride;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.UberAppUtil;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Restrictions;
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

        UUID rideId = UUID.fromString(req.params(":id"));
        Ride ride = Repositories.rides().get(rideId);

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
                return e.getMessage();
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
            return e.getMessage();
        }
    };

    public static Route update = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        UUID rideId = UUID.fromString(req.params(":id"));
        Ride ride = Repositories.rides().get(rideId);
        Ride validationRide = (Ride) ride.clone();

        try{
            ObjectMapper mapper = new ObjectMapper();
            Ride updatedRide = mapper.readValue(req.body(), Ride.class);


            try{
                validationRide.isValid();
            }catch (Exception e){
                session.stop();
                res.status(400);
                return e.getMessage();
            }

            session.stop();
            res.status(200);
            res.type("application/json");
            return dataToJson("Ride Updated");

        }catch (JsonParseException e){
            session.stop();
            res.status(400);
            res.type("application/json");
            return e.getMessage();
        }
    };

    public static Route delById = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        UUID rideId = UUID.fromString(req.params(":id"));
        Ride ride = Repositories.rides().get(rideId);
        Repositories.rides().delete(ride);

        session.stop();
        res.status(200);
        res.type("application/json");
        return dataToJson("Ride Deleted");
    };

    public static Route addRoutePoints = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        try{
            ObjectMapper mapper = new ObjectMapper();
            RoutePoint routePoint = mapper.readValue(req.body(), RoutePoint.class);

            UUID rideId = UUID.fromString(req.params(":id"));
            Ride ride = Repositories.rides().get(rideId);

            try {
                ride.isValid();
                routePoint.isValid();
            } catch (Exception e){
                res.status(400);
                return e.getMessage();
            }

            routePoint.setId(UUID.randomUUID());
            Repositories.routePoints().add(routePoint);

            session.stop();
            res.status(201);
            res.type("application/json");
            return dataToJson(routePoint);

        }catch (JsonParseException e){
            session.stop();
            res.status(400);
            res.type("application/json");
            return e.getMessage();
        }
    };

    public static Route getRoutePoints = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        UUID rideId = UUID.fromString(req.params(":id"));
        List<RoutePoint> routePoints = Repositories.routePoints().find(Restrictions.equals("rideId", rideId));

        session.stop();
        res.status(200);
        res.type("application/json");
        return dataToJson(routePoints);
    };
}
