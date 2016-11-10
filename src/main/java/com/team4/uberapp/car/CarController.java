package com.team4.uberapp.car;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.domain.Repositories;

import com.team4.uberapp.persistence.*;
import com.team4.uberapp.driver.Driver;
import org.mongolink.MongoSession;

import com.team4.uberapp.util.JsonUtil;
import spark.Route;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by HectorGuo on 11/8/16.
 */
public class CarController {
    // GET /cars  Get all cars
    public static Route getAll = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        List<Car> cars = Repositories.cars().all();

        /* close database connection */
        session.stop();

        res.status(200);
        res.type("application/json");
        if (cars.size() == 0) {
            return "No cars";
        } else {
            //return car.size();
            //return JsonUtil.dataToJson(car);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(cars);
        }

    };


    // GET /cars/:id  Get car by id
    public static Route getById = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get car by id, generate UUID from string id first
        UUID uid = UUID.fromString(req.params(":id"));
        Car car = Repositories.cars().get(uid);

        // close database connection
        session.stop();

        res.type("application/json");
        if (car == null) {
            res.status(404); // 404 Not found
            return "Car: " + req.params(":id") +" not found";
        } else {
            res.status(200);
            return JsonUtil.dataToJson(car);
        }
    };

    // POST /cars  Create car
    public static Route create = (req, res) -> {
        /* initialize db connection */
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        try {
//            ObjectMapper mapper = new ObjectMapper();
//            Car car = mapper.readValue(req.body(), Car.class);

            Car car = new Car("foo", "foo", "foo","foo",0,"foo" );
            int i =0;
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(req.body());
            Gson gson = new Gson();

            // get make
            if (jsonObject.get("make") != null) {
                car.setMake(gson.fromJson(jsonObject.get("make"), String.class));
                i = 1;
            }
            // get model
            if (jsonObject.get("model") != null) {
                car.setMake(gson.fromJson(jsonObject.get("model"), String.class));
                i = 1;
            }
            // get license
            if (jsonObject.get("license") != null) {
                car.setLicense(gson.fromJson(jsonObject.get("license"), String.class));
                i = 1;
            }

            // patch carType
            if (jsonObject.get("carType") != null) {
                car.setCarType(gson.fromJson(jsonObject.get("carType"), String.class));
                i = 1;
            }

            // patch validRideTypes
            if (jsonObject.get("validRideTypes") != null) {
                car.setValidRideTypes(gson.fromJson(jsonObject.get("validRideTypes"), String.class));
                i = 1;
            }

            // patch maxPassengers
            if (jsonObject.get("maxPassengers") != null) {
                car.setMaxPassengers(gson.fromJson(jsonObject.get("maxPassengers"), Integer.class));
                i = 1;
            }

            try {
                car.isValid();
            } catch (Exception e){
                res.status(400);
                return JsonUtil.dataToJson(e.getMessage());
            }

            Repositories.cars().add(car);

            // close database connection
            session.stop();

            //prepare return result
            res.status(200);
            res.type("application/json");
            return JsonUtil.dataToJson(car);
        }  catch (Exception e){
            session.stop();
            res.status(400);
            return e.getMessage();
        }
    };


    // DELETE /cars/:id  Delete car by id
    public static Route delById = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get car by id, generate UUID from string id first
        UUID uid = UUID.fromString(req.params(":id"));
        Car car = Repositories.cars().get(uid);

        res.type("application/json");
        if (car == null) {
            res.status(404); // 404 Not found
            // close database connection
            session.stop();
            return "Car: " + req.params(":id") +" not found";
        } else {
            Repositories.cars().delete(car);
            // close database connection
            session.stop();
            res.status(200);
            return "Car: " + req.params(":id") +" deleted";
        }
    };

    // PATCH /cars/:id  Update car by id
    public static Route update = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get car by id, generate UUID from string id first
        UUID uid = UUID.fromString(req.params(":id"));
        Car car = Repositories.cars().get(uid);

        if (car == null) {
            res.status(404); // 404 Not found
            // close database connection
            session.stop();
            return "Car: " + req.params(":id") +" not found";
        } else {
            int i =0;
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(req.body());
            Gson gson = new Gson();

            // get make
            if (jsonObject.get("make") != null) {
                car.setMake(gson.fromJson(jsonObject.get("make"), String.class));
                i = 1;
            }
            // get model
            if (jsonObject.get("model") != null) {
                car.setMake(gson.fromJson(jsonObject.get("model"), String.class));
                i = 1;
            }
            // get license
            if (jsonObject.get("license") != null) {
                car.setLicense(gson.fromJson(jsonObject.get("license"), String.class));
                i = 1;
            }

            // patch carType
            if (jsonObject.get("carType") != null) {
                car.setCarType(gson.fromJson(jsonObject.get("carType"), String.class));
                i = 1;
            }

            // patch validRideTypes
            if (jsonObject.get("validRideTypes") != null) {
                car.setValidRideTypes(gson.fromJson(jsonObject.get("validRideTypes"), String.class));
                i = 1;
            }

            // patch maxPassengers
            if (jsonObject.get("maxPassengers") != null) {
                car.setMaxPassengers(gson.fromJson(jsonObject.get("maxPassengers"), Integer.class));
                i = 1;
            }

            session.stop();
            res.status(200);
            if (i !=0) {
                return "Car: " + req.params(":id") +" updated" ;
            } else {
                return "Car" + req.params(":id") + "incorrect params" + req.body();
            }
        }
    };

    // POST /drivers/:driverId/cars  Create car
    public static Route createByDriverId = (req, res) -> {
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get Driver by id, generate UUID from string id first
        UUID driverId = UUID.fromString(req.params(":driverId"));
        Driver driver = Repositories.drivers().get(driverId);

        if(driver == null) {
            res.status(404);
            return "Driver: " + driverId +" not found";
        }

        try{
            ObjectMapper mapper = new ObjectMapper();
            Car car = mapper.readValue(req.body(), Car.class);

            try {
                car.isValid();
            } catch (Exception e){
                res.status(400);
                return JsonUtil.dataToJson(e.getMessage());
            }

            car.setId(UUID.randomUUID());
            car.setDriverId(driverId);
            Repositories.cars().add(car);

            session.stop();
            res.status(201);
            res.type("application/json");
            return JsonUtil.dataToJson(car);

        }catch (JsonParseException e){
            session.stop();
            res.status(400);
            res.type("application/json");
            return JsonUtil.dataToJson(e.getMessage());
        }
    };

    // GET /drivers/:driverId/cars  Get car
    public static Route getByDriverId = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        UUID driverId = UUID.fromString(req.params(":driverId"));
        List<Car> cars = Repositories.cars().all();

        List<Car> matchedCar = cars
                .stream()
                .filter((car) -> {
                    if(car.getDriverId() == null) {
                        return false;
                    }
                    return driverId.equals(car.getDriverId());
                })
                .collect(Collectors.toList());;
        /* close database connection */
        session.stop();

        res.status(200);
        if (matchedCar.size() == 0) {
            return "No cars";
        } else {
            res.type("application/json");
            return JsonUtil.dataToJson(matchedCar);
        }
    };
}
