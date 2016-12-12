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

    /**
     * GET /rides  Get all rides
     * @return List<Ride> a list of rides
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
     * GET /rides/:id  Get one ride by id
     * @return Ride  one ride
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
     * POST /rides  Create ride
     * {
     *  "rideType":"ECONOMY",
     *  "startLat": 10.22,
     *  "startLong": 0,
     *  "endLat": 0.22,
     *  "endLong": 0,
     *  "requestTime":1480131627374,
     *  "pickupTime":1480131657374,
     *  "dropOffTime":1480131697374,
     *  "status":"AWAITING_DRIVER",
     *  "fare":5,
     *  "driverId":"9e584258-554c-4275-abba-6f02c3ab7476",
     *  "carId":"b462bcae-2add-4b59-8c18-107a30c1e87e",
     *  "passengerId":"a57536e5-c05c-437a-9ef9-d27a267a7d85"
     * }
     * @return Ride  info for a ride which has created
     */
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
     * POST /rides/:id  Update ride by id
     * {
     *  "rideType":"ECONOMY",
     *  "startLat": 10.22,
     *  "startLong": 0,
     *  "endLat": 0.22,
     *  "endLong": 0,
     *  "requestTime":1480131627374,
     *  "pickupTime":1480131657374,
     *  "dropOffTime":1480131697374,
     *  "status":"AWAITING_DRIVER",
     *  "fare":5,
     *  "driverId":"9e584258-554c-4275-abba-6f02c3ab7476",
     *  "carId":"b462bcae-2add-4b59-8c18-107a30c1e87e",
     *  "passengerId":"a57536e5-c05c-437a-9ef9-d27a267a7d85"
     * }
     * @return Ride  info for a ride which has updated
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

            // carID
            if (updatedRide.getCarId() != null) {
                //if (!updatedRide.getCarId()..isEmpty())
                {
                    validationRide.setCarId(updatedRide.getCarId());
                }
            }
            // driverId
            if (updatedRide.getDriverId() != null) {
                //if (!updatedRide.getCarId()..isEmpty())
                {
                    validationRide.setDriverId(updatedRide.getDriverId());
                }
            }
            // passengerId
            if (updatedRide.getPassengerId() != null) {
                //if (!updatedRide.getCarId()..isEmpty())
                {
                    validationRide.setPassengerId(updatedRide.getPassengerId());
                }
            }
            // status
            if (updatedRide.getStatus() != null) {
                if (!updatedRide.getStatus().isEmpty())
                {
                    validationRide.setStatus(updatedRide.getStatus());
                }
            }
            // rideType
            if (updatedRide.getRideType() != null) {
                if (!updatedRide.getRideType().isEmpty()) {
                    validationRide.setRideType(updatedRide.getRideType());
                }
            }
            // startLat
            if (updatedRide.getStartLat() != null) {
                    validationRide.setStartLat(updatedRide.getStartLat());
            }
            // startLong
            if (updatedRide.getStartLong() != null) {
                validationRide.setStartLong( updatedRide.getStartLong());
            }
            // endLat
            if (updatedRide.getEndLat() != null) {
                validationRide.setEndLat(updatedRide.getEndLat());
            }
            // endLong
            if (updatedRide.getEndLong() != null) {
                validationRide.setEndLong(updatedRide.getEndLong());
            }
            // pickupTime
            if (updatedRide.getPickupTime() != null) {
                validationRide.setPickupTime(updatedRide.getPickupTime());
            }
            // requestTime
            if (updatedRide.getRequestTime() != null) {
                validationRide.setRequestTime(updatedRide.getRequestTime());
            }
            // dropOffTime
            if (updatedRide.getDropOffTime() != null) {
                validationRide.setDropOffTime(updatedRide.getDropOffTime());
            }
            // fair
            if (updatedRide.getFare() !=0) {
                validationRide.setFare(updatedRide.getFare());
            }

            try{
                validationRide.isValid();
            }catch (Exception e){
                session.stop();
                res.status(400);
                return e.getMessage();
            }
            //update value
            ride.setRideType(validationRide.getRideType());
            ride.setStartLat(validationRide.getStartLat());
            ride.setStartLong(validationRide.getStartLong());
            ride.setEndLat(validationRide.getEndLat());
            ride.setEndLong(validationRide.getEndLong());
            ride.setRequestTime(validationRide.getRequestTime());
            ride.setPickupTime(validationRide.getPickupTime());
            ride.setDropOffTime(validationRide.getDropOffTime());
            ride.setStatus(validationRide.getStatus());
            ride.setFare(validationRide.getFare());
            ride.setDriverId(validationRide.getDriverId());
            ride.setPassengerId(validationRide.getPassengerId());
            ride.setCarId(validationRide.getCarId());

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

    /**
     * DELETE /rides/:id  Delete ride by id
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
     * POST /rides/:id/routePoints  add route points
     * {
     *  "lat": 22.11,
     *  "lng": 11.11,
     *  "timestamp": 1480131657374
     * }
     * @return <RoutePoint>  one route point with id
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
     * GET /rides/:id/routePoints  Get route points by ride id
     * @return List<RoutePoint> a list of route points
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
