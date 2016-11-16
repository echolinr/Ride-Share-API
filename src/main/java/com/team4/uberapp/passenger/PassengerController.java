package com.team4.uberapp.passenger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.UberAppUtil;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Order;
import org.mongolink.domain.criteria.Restrictions;
import spark.Route;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by lzhai on 2016/11/10.
 */
public class PassengerController extends UberAppUtil {
    // GET /passengers  Get all passengers
    public static Route getAll = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        List<Passenger> passengers;

        if (req.queryParams().isEmpty()) {
            passengers = Repositories.passengers().all();
        } else {
            Criteria criteria = session.createCriteria(Passenger.class); // create criteria object
            final List<String> queryFields = Arrays.asList("count", "offsetId", "sort", "sortOrder");
            Set<String> queryParams = req.queryParams();
            String querySort = null;
            String querySortOrder = null;
            for(String param : queryParams){
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
            passengers = criteria.list();
        }
        /* close database connection */
        session.stop();
        res.status(200);
        return dataToJson(passengers);

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
            return dataToJson("Passenger: " + req.params(":id") +" not found");
        } else {
            res.status(200);
            return dataToJson(passenger);
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
            Passenger passenger = mapper.readValue(req.body(), Passenger.class);
            try {
                passenger.isValid();
            } catch (Exception e){
                res.status(400);
                res.type("application/json");
                return dataToJson(e.getMessage());
            }

            Criteria criteria = session.createCriteria(Passenger.class); // create criteria object
            criteria.add(Restrictions.equals("emailAddress", passenger.getEmailAddress()));
            // emailAddress for driver must be unique
            if (criteria.list() == null || criteria.list().isEmpty()) {
                passenger.setId(UUID.randomUUID()); //generate UUID
                passenger.setPassword(hashPassword(passenger.getPassword()));
                session.clear();
                Repositories.passengers().add(passenger);

                session.stop();
                res.status(201);
                res.type("application/json");
                return dataToJson(passenger);
            } else {
                session.stop();
                res.status(400);
                res.type("application/json");
                return dataToJson("Passenger has conflict email address： " + passenger.getEmailAddress());
            }
        }  catch (Exception e){
            session.stop();
            res.type("application/json");
            res.status(400);
            return dataToJson(e.getMessage());
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
            return dataToJson("Passenger: " + req.params(":id") +" not found");
        } else {
            Repositories.passengers().delete(passenger);
            // close database connection
            session.stop();
            res.type("application/json");
            res.status(200);
            return dataToJson("Passenger: " + req.params(":id") +" deleted");
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
            return dataToJson("Passenger: " + req.params(":id") +" not found");
        } else {
            // clone a passenger for validation purpose
            Passenger validationPassenger = (Passenger) passenger.clone();

            try {
                ObjectMapper mapper = new ObjectMapper();
                Passenger updatePassenger = mapper.readValue(req.body(), Passenger.class);

                // firstName
                if (updatePassenger.getFirstName() != null) {
                    if (!updatePassenger.getFirstName().isEmpty()) {
                        validationPassenger.setFirstName(updatePassenger.getFirstName());
                    }
                }
                // lastName
                if (updatePassenger.getLastName() != null) {
                    if (!updatePassenger.getLastName().isEmpty()) {
                        validationPassenger.setLastName(updatePassenger.getLastName());
                    }
                }
                // emailAddress
                if (updatePassenger.getEmailAddress() != null) {
                    if (!updatePassenger.getEmailAddress().isEmpty()) {
                        validationPassenger.setEmailAddress(updatePassenger.getEmailAddress());
                    }
                }
                // password: we may need special handle on password later
                if (updatePassenger.getPassword() != null) {
                    if (!updatePassenger.getPassword().isEmpty()) {
                        validationPassenger.setPassword(updatePassenger.getPassword());
                    }
                }

                // addressLine1
                if (updatePassenger.getAddressLine1() != null) {
                    if (!updatePassenger.getAddressLine1().isEmpty()) {
                        validationPassenger.setAddressLine1(updatePassenger.getAddressLine1());
                    }
                }
                // addressLine2
                if (updatePassenger.getAddressLine2() != null) {
                    if (!updatePassenger.getAddressLine2().isEmpty()) {
                        validationPassenger.setAddressLine2(updatePassenger.getAddressLine2());
                    }
                }
                // city
                if (updatePassenger.getCity() != null) {
                    if (!updatePassenger.getCity().isEmpty()) {
                        validationPassenger.setCity(updatePassenger.getCity());
                    }
                }
                // state
                if (updatePassenger.getState() != null) {
                    if (!updatePassenger.getState().isEmpty()) {
                        validationPassenger.setState(updatePassenger.getState());
                    }
                }
                // zip
                if (updatePassenger.getZip() != null) {
                    if (!updatePassenger.getZip().isEmpty()) {
                        validationPassenger.setZip(updatePassenger.getZip());
                    }
                }
                // phoneNumber
                if (updatePassenger.getPhoneNumber() != null) {
                    if (!updatePassenger.getPhoneNumber().isEmpty()) {
                        validationPassenger.setPhoneNumber(updatePassenger.getPhoneNumber());
                    }
                }

                //validation
                try {
                    validationPassenger.isValid();
                } catch (Exception e) {
                    session.stop();
                    res.type("application/json");
                    return  dataToJson(e.getMessage());
                }

                //update value
                passenger.setFirstName(validationPassenger.getFirstName());
                passenger.setLastName(validationPassenger.getLastName());
                passenger.setEmailAddress(validationPassenger.getEmailAddress());
                passenger.setPassword(validationPassenger.getPassword());
                passenger.setAddressLine1(validationPassenger.getAddressLine1());
                passenger.setAddressLine2(validationPassenger.getAddressLine2());
                passenger.setCity(validationPassenger.getCity());
                passenger.setState(validationPassenger.getState());
                passenger.setZip(validationPassenger.getZip());
                passenger.setPhoneNumber(validationPassenger.getPhoneNumber());
                session.stop();
                res.type("application/json");
                return dataToJson("Passenger:" + req.params(":id") +" updated!");
            } catch (JsonParseException e) {
                session.stop();
                res.type("application/json");
                res.status(400);
                return dataToJson(e.getMessage());
            }
/*
            int i =0;
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(req.body());
            Gson gson = new Gson();

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
*/
        }
    };

}
