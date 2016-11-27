/**
 * Ride Controller, used for abstracting CRUD methods of rides
 *
 * @author  Hector Guo, Lin Zhai
 * @version 1.0
 * @since   2016-11-18
 */
package com.team4.uberapp.ride;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.location.Coordinate;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.UberAppUtil;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Restrictions;
import spark.Route;
import com.google.gson.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by HectorGuo on 11/8/16.
 */
public class RideController extends UberAppUtil {

    /**
     * The Spark Route getAll.
     */
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

    /**
     * The Spark Route getById.
     */
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

    /**
     * The Spark Route create.
     */
    public static Route create = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();

        session.start();
        Repositories.initialise(new MongoRepositories(session));

        try{
            ObjectMapper mapper = new ObjectMapper();
            Ride ride = mapper.readValue(req.body(), Ride.class);
//            Gson gson = new Gson();
//            JsonObject rideJson = gson.fromJson(req.body(), JsonObject.class);
//
//            // get startPoint and endPoint
//            JsonElement start = rideJson.getAsJsonObject("startPoint");
//            float startLat = start.getAsJsonObject().get("lat").getAsFloat();
//            float startLng = start.getAsJsonObject().get("long").getAsFloat();
//            JsonElement end = rideJson.getAsJsonObject("endPoint");
//            float endLat = end.getAsJsonObject().get("lat").getAsFloat();
//            float endLng = end.getAsJsonObject().get("long").getAsFloat();
//
//            // get timestamp
//            Number requestTime = rideJson.get("requestTime").getAsNumber();
//            Number pickupTime = rideJson.get("pickupTime").getAsNumber();
//            Number dropOffTime = rideJson.get("dropOffTime").getAsNumber();
//
//            // initial ride
//            Ride ride = new Ride();
//
//            ride.setCarId(UUID.fromString(rideJson.get("carId").getAsString()));
//            ride.setDriverId(UUID.fromString(rideJson.get("driverId").getAsString()));
//            ride.setPassengerId(UUID.fromString(rideJson.get("passengerId").getAsString()));
//
//            ride.setStartPoint(new Coordinate(startLat, startLng));
//            ride.setEndPoint(new Coordinate(endLat, endLng));
//
//            ride.setRequestTime(requestTime);
//            ride.setDropOffTime(dropOffTime);
//            ride.setPickupTime(pickupTime);
//
//            ride.setRideType(rideJson.get("rideType").getAsString());
//            ride.setStatus(rideJson.get("status").getAsString());
//            ride.setFare(rideJson.get("fare").getAsInt());

            try {
                ride.isValid();
            } catch (Exception e){
                res.status(400);
                res.type("application/json");
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

    /**
     * The Spark Route update.
     */
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

    /**
     * The Spark Route delById.
     */
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

    /**
     * The Spark Route addRoutePoints.
     */
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

            routePoint.setRideId(rideId);
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

    /**
     * The Spark Route getRoutePoints.
     */
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
