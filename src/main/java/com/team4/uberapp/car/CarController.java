package com.team4.uberapp.car;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.driver.Driver;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.UberAppUtil;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Order;
import spark.Route;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by HectorGuo on 11/8/16.
 */
public class CarController extends UberAppUtil {
    // GET /cars  Get all cars
    /**
     * Implementation  for route:
     *      //GET/cars  -- get all cars
     *      //Get/cars for querying parameters count, offsetId, sort & sortOrder
     *      Used in combination with sort, it specifies the order in which to return the elements. asc is for asending
     *      or desc for descending. Default value is asc except for a time-based sort field in which case the default values is desc
     *
     * @return AppUser - userID in token if not expired or null
     */
    public static Route getAll = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        List<Car> cars;

        if (req.queryParams().isEmpty()) {
            cars = Repositories.cars().all();
        } else {
            Criteria criteria = session.createCriteria(Car.class); // create criteria object
            final List<String> queryFields = Arrays.asList("count", "offsetId", "sort", "sortOrder");
            Set<String> queryParams = req.queryParams();
            //StringBuilder str = new StringBuilder();
            String querySort = null;
            String querySortOrder = null;
            //str.append("Request Parameters are <br/>");
            for(String param : queryParams){
                //str.append(param).append(" ").append(req.queryParams(param)).append("<br />"); // build debug message
                if (!queryFields.contains(param)) {
                    session.stop();
                    res.status(200);
                    res.type("applicaiton/json");
                    return dataToJson("Wrong query params :" + param);
                }
                if (param.compareTo("count") == 0)  {
                    criteria.limit(Integer.parseInt(req.queryParams(param)));
                } else if (param.compareTo("offsetId") == 0) {
                    criteria.skip(Integer.parseInt(req.queryParams(param)));
                } else if (param.equalsIgnoreCase("sort") == true){
                    querySort = new String(req.queryParams(param));
                } else if (param.equalsIgnoreCase("sortOrder") == true) {
                    querySortOrder = new String(req.queryParams(param));
                }
            }
            // setup sort and sortOrder
            if (querySort != null && querySortOrder != null) {
                if (querySortOrder.equalsIgnoreCase("asc") == true) {
                    criteria.sort(querySort, Order.ASCENDING);
                } else {
                    criteria.sort(querySort, Order.DESCENDING);
                }
            } else if (((querySort != null) && (querySortOrder == null)) ||
                    ((querySort == null) && (querySortOrder != null)) ){
                session.stop();
                res.status(200);
                res.type("applicaiton/json");
                return dataToJson("sort & sortOrder params must be in pair.");
            }
            cars = criteria.list();
        }

        // close database connection
        session.stop();

        res.status(200);
        res.type("application/json");
        if (cars.size() == 0) {
            return dataToJson("No cars");
        } else {
            return dataToJson(cars);
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
            return dataToJson("Car: " + req.params(":id") +" not found");
        } else {
            res.status(200);
            return dataToJson(car);
        }
    };

    // POST /cars  Create car
    public static Route create = (req, res) -> {
        /* initialize db connection */
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        try {
            ObjectMapper mapper = new ObjectMapper();
            Car car = mapper.readValue(req.body(), Car.class);
            car.setId(UUID.randomUUID());

/*
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
*/
            try {
                car.isValid();
            } catch (Exception e){
                res.status(400);
                return e.getMessage();
            }

            Repositories.cars().add(car);

            // close database connection
            session.stop();

            //prepare return result
            res.type("application/json");
            res.status(200);
            return dataToJson(car);
        }  catch (Exception e){
            session.stop();
            res.type("application/json");
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
            return dataToJson("Car: " + req.params(":id") +" not found");
        } else {
            Repositories.cars().delete(car);
            // close database connection
            session.stop();
            res.status(200);
            return dataToJson("Car: " + req.params(":id") +" deleted");
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
            return dataToJson("Car: " + req.params(":id") +" not found");
        } else {
            /*
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
            */

            // clone a passenger for validation purpose
            Car validationCar = (Car) car.clone();
            try {
                ObjectMapper mapper = new ObjectMapper();
                Car updateCar = mapper.readValue(req.body(), Car.class);

                // make
                if (updateCar.getMake() != null) {
                    if (!updateCar.getMake().isEmpty()) {
                        validationCar.setMake(updateCar.getMake());
                    }
                }
                // model
                if (updateCar.getModel() != null) {
                    if (!updateCar.getModel().isEmpty()) {
                        validationCar.setModel(updateCar.getModel());
                    }
                }
                // license
                if (updateCar.getCarType() != null) {
                    if (!updateCar.getCarType().isEmpty()) {
                        validationCar.setCarType(updateCar.getCarType());
                    }
                }
                // color
                if (updateCar.getColor() != null) {
                    if (!updateCar.getColor().isEmpty()) {
                        validationCar.setColor(updateCar.getColor());
                    }
                }
                // validRideTypes
                if (updateCar.getValidRideTypes() != null) {
                    if (!updateCar.getValidRideTypes().isEmpty()) {
                        validationCar.setValidRideTypes(updateCar.getValidRideTypes());
                    }
                }
                // maxPassenger
                if (updateCar.getMaxPassengers() != 0) {
                    validationCar.setMaxPassengers(updateCar.getMaxPassengers());
                }

                //validation
                try {
                    validationCar.isValid();
                } catch (Exception e) {
                    session.stop();
                    res.type("application/json");
                    return  e.getMessage();
                }

                //update value
                car.setMake(validationCar.getMake());
                car.setModel(validationCar.getModel());
                car.setLicense(validationCar.getLicense());
                car.setCarType(validationCar.getCarType());
                car.setMaxPassengers(validationCar.getMaxPassengers());
                car.setColor(validationCar.getColor());
                car.setValidRideTypes(validationCar.getValidRideTypes());
                session.stop();
                res.type("application/json");
                return dataToJson("Car: " + req.params(":id") +" updated") ;
            } catch (JsonParseException e) {
                session.stop();
                res.type("application/json");
                res.status(400);
                return e.getMessage();
            }
        }
    };

    // POST /drivers/:driverId/cars  Create car
    public static Route createByDriverId = (req, res) -> {
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get Ride by id, generate UUID from string id first
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
                return e.getMessage();
            }

            car.setId(UUID.randomUUID());
            car.setDriverId(driverId);
            Repositories.cars().add(car);

            session.stop();
            res.status(201);
            res.type("application/json");
            return dataToJson(car);

        }catch (JsonParseException e){
            session.stop();
            res.status(400);
            res.type("application/json");
            return e.getMessage();
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
            return dataToJson(matchedCar);
        }
    };
}
