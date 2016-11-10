package com.team4.uberapp.passenger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.JsonUtil;
import org.mongolink.MongoSession;
import spark.Route;

import java.util.List;
import java.util.UUID;

/**
 * Created by lzhai on 2016/11/10.
 */
public class PassengerController {
    // GET /passengers  Get all passengers
    public static Route getAll = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        List<Passenger> passengers = Repositories.passengers().all();

            /* close database connection */
        session.stop();
        res.status(200);
        return JsonUtil.dataToJson(passengers);

    };


    // GET /passengers/:id  Get passenger by id
    public static Route getById = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get car by id, generate UUID from string id first
        UUID uid = UUID.fromString(req.params(":id"));
        Passenger passenger = Repositories.passengers().get(uid);

        // close database connection
        session.stop();

        res.type("application/json");
        if (passenger == null) {
            res.status(404); // 404 Not found
            return JsonUtil.dataToJson("Passenger: " + req.params(":id") +" not found");
        } else {
            res.status(200);
            return JsonUtil.dataToJson(passenger);
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

            Passenger passenger = new Passenger("foo", "foo", "foo@foo.com","foofoofoo","foo","foo","foo","null","00000","123-456-7890" );
            int i =0;
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(req.body());
            Gson gson = new Gson();

            // get firstName
            if (jsonObject.get("firstName") != null) {
                passenger.setFirstName(gson.fromJson(jsonObject.get("firstName"), String.class));
                i = 1;
            }
            // get lastName
            if (jsonObject.get("lastName") != null) {
                passenger.setLastName(gson.fromJson(jsonObject.get("lastName"), String.class));
                i = 1;
            }
            // get emailAddress
            if (jsonObject.get("emailAddress") != null) {
                passenger.setEmailAddress(gson.fromJson(jsonObject.get("emailAddress"), String.class));
                i = 1;
            }

            // get password: should convert to hash?
            if (jsonObject.get("password") != null) {
                passenger.setPassword(gson.fromJson(jsonObject.get("password"), String.class));
                i = 1;
            }

            // get addressLine1
            if (jsonObject.get("addressLine1") != null) {
                passenger.setAddressLine1(gson.fromJson(jsonObject.get("addressLine1"), String.class));
                i = 1;
            }

            // get addressLine2
            if (jsonObject.get("addressLine2") != null) {
                passenger.setAddressLine2(gson.fromJson(jsonObject.get("addressLine2"), String.class));
                i = 1;
            }

            // get city
            if (jsonObject.get("city") != null) {
                passenger.setCity(gson.fromJson(jsonObject.get("city"), String.class));
                i = 1;
            }

            // get state
            if (jsonObject.get("state") != null) {
                passenger.setState(gson.fromJson(jsonObject.get("state"), String.class));
                i = 1;
            }

            // get zip
            if (jsonObject.get("zip") != null) {
                passenger.setZip(gson.fromJson(jsonObject.get("zip"), String.class));
                i = 1;
            }

            // get state
            if (jsonObject.get("phoneNumber") != null) {
                passenger.setPhoneNumber(gson.fromJson(jsonObject.get("phoneNumber"), String.class));
                i = 1;
            }

            try {
                passenger.isValid();
            } catch (Exception e){
                res.status(400);
                res.type("application/json");
                return JsonUtil.dataToJson(e.getMessage());
            }

            // after validation, we should convert password to hash????

            Repositories.passengers().add(passenger);

            // close database connection
            session.stop();

            //prepare return result
            res.type("application/json");
            res.status(200);
            return JsonUtil.dataToJson(passenger);
        }  catch (Exception e){
            session.stop();
            res.type("application/json");
            res.status(400);
            return JsonUtil.dataToJson(e.getMessage());
        }
    };


    // DELETE /passengers/:id  Delete passenger by id
    public static Route delById = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get car by id, generate UUID from string id first
        UUID uid = UUID.fromString(req.params(":id"));
        Passenger passenger = Repositories.passengers().get(uid);

        if (passenger == null) {
            // close database connection
            session.stop();
            res.type("application/json");
            res.status(404); // 404 Not found
            return JsonUtil.dataToJson("Passenger: " + req.params(":id") +" not found");
        } else {
            Repositories.passengers().delete(passenger);
            // close database connection
            session.stop();
            res.type("application/json");
            res.status(200);
            return JsonUtil.dataToJson("Passenger: " + req.params(":id") +" deleted");
        }
    };
    // PATCH /passengers/:id  Update passenger by id
    public static Route update = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get car by id, generate UUID from string id first
        UUID uid = UUID.fromString(req.params(":id"));
        Passenger passenger = Repositories.passengers().get(uid);


        if (passenger == null) {
            // close database connection
            session.stop();
            res.status(404); // 404 Not found
            res.type("application/json");
            return JsonUtil.dataToJson("Passenger: " + req.params(":id") +" not found");
        } else {
            int i =0;
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(req.body());
            Gson gson = new Gson();

            // get firstName
            if (jsonObject.get("firstName") != null) {
                passenger.setFirstName(gson.fromJson(jsonObject.get("firstName"), String.class));
                i = 1;
            }
            // get lastName
            if (jsonObject.get("lastName") != null) {
                passenger.setLastName(gson.fromJson(jsonObject.get("lastName"), String.class));
                i = 1;
            }
            // get emailAddress
            if (jsonObject.get("emailAddress") != null) {
                passenger.setEmailAddress(gson.fromJson(jsonObject.get("emailAddress"), String.class));
                i = 1;
            }

            // get password: should convert to hash?
            if (jsonObject.get("password") != null) {
                passenger.setPassword(gson.fromJson(jsonObject.get("password"), String.class));
                i = 1;
            }

            // get addressLine1
            if (jsonObject.get("addressLine1") != null) {
                passenger.setAddressLine1(gson.fromJson(jsonObject.get("addressLine1"), String.class));
                i = 1;
            }

            // get addressLine2
            if (jsonObject.get("addressLine2") != null) {
                passenger.setAddressLine2(gson.fromJson(jsonObject.get("addressLine2"), String.class));
                i = 1;
            }

            // get city
            if (jsonObject.get("city") != null) {
                passenger.setCity(gson.fromJson(jsonObject.get("city"), String.class));
                i = 1;
            }

            // get state
            if (jsonObject.get("state") != null) {
                passenger.setState(gson.fromJson(jsonObject.get("state"), String.class));
                i = 1;
            }

            // get zip
            if (jsonObject.get("zip") != null) {
                passenger.setZip(gson.fromJson(jsonObject.get("zip"), String.class));
                i = 1;
            }

            // get state
            if (jsonObject.get("phoneNumber") != null) {
                passenger.setPhoneNumber(gson.fromJson(jsonObject.get("phoneNumber"), String.class));
                i = 1;
            }

            session.stop();
            res.type("application/json");
            res.status(200);
            if (i !=0) {

                return JsonUtil.dataToJson("Passenger: " + req.params(":id") +" updated");
            } else {
                return JsonUtil.dataToJson("Passenger" + req.params(":id") + "incorrect params" + req.body());
            }
        }
    };

}
