package com.team4.uberapp.driver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.MongoConfiguration;
import org.mongolink.MongoSession;
import com.team4.uberapp.persistence.MongoRepositories;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Order;
import spark.Route;
import com.team4.uberapp.util.JsonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by HectorGuo on 11/8/16.
 */
public class DriverController extends JsonUtil {
    public static Route getAll = (req, res) -> {
        final MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        List<Driver> drivers;

        if (req.queryParams().isEmpty()) {
            drivers = Repositories.drivers().all();
        } else {
            Criteria criteria = session.createCriteria(Driver.class); // create criteria object
            final List<String> queryFields = Arrays.asList("count", "offsetId", "sort", "sortOrder");
            Set<String> queryParams = req.queryParams();
            String querySort = null;
            String querySortOrder = null;
            for(String param : queryParams){
                if (!queryFields.contains(param)) {
                    session.stop();
                    res.status(200);
                    res.type("applicaiton/json");
                    return JsonUtil.dataToJson("Wrong query params :" + param);
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
                return JsonUtil.dataToJson("sort & sortOrder params must be in pair.");
            }
            drivers = criteria.list();
        }
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
        if (driver == null) {
            res.status(404); // 404 Not found
            return JsonUtil.dataToJson("Driver: " + req.params(":id") +" not found");
        } else {
            res.status(200);
            return JsonUtil.dataToJson(driver);
        }
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

            // firstName
            if (updatedDriver.getFirstName() != null) {
                if (!updatedDriver.getFirstName().isEmpty()) {
                    validationDriver.setFirstName(updatedDriver.getFirstName());
                }
            }
            // lastName
            if (updatedDriver.getLastName() != null) {
                if (!updatedDriver.getLastName().isEmpty()) {
                    validationDriver.setLastName(updatedDriver.getLastName());
                }
            }
            // emailAddress
            if (updatedDriver.getEmailAddress() != null) {
                if (!updatedDriver.getEmailAddress().isEmpty()) {
                    validationDriver.setEmailAddress(updatedDriver.getEmailAddress());
                }
            }
            // password: we may need special handle on password later
            if (updatedDriver.getPassword() != null) {
                if (!updatedDriver.getPassword().isEmpty()) {
                    validationDriver.setPassword(updatedDriver.getPassword());
                }
            }

            // addressLine1
            if (updatedDriver.getAddressLine1() != null) {
                if (!updatedDriver.getAddressLine1().isEmpty()) {
                    validationDriver.setAddressLine1(updatedDriver.getAddressLine1());
                }
            }
            // addressLine2
            if (updatedDriver.getAddressLine2() != null) {
                if (!updatedDriver.getAddressLine2().isEmpty()) {
                    validationDriver.setAddressLine2(updatedDriver.getAddressLine2());
                }
            }
            // city
            if (updatedDriver.getCity() != null) {
                if (!updatedDriver.getCity().isEmpty()) {
                    validationDriver.setCity(updatedDriver.getCity());
                }
            }
            // state
            if (updatedDriver.getState() != null) {
                if (!updatedDriver.getState().isEmpty()) {
                    validationDriver.setState(updatedDriver.getState());
                }
            }
            // zip
            if (updatedDriver.getZip() != null) {
                if (!updatedDriver.getZip().isEmpty()) {
                    validationDriver.setZip(updatedDriver.getZip());
                }
            }
            // phoneNumber
            if (updatedDriver.getPhoneNumber() != null) {
                if (!updatedDriver.getPhoneNumber().isEmpty()) {
                    validationDriver.setPhoneNumber(updatedDriver.getPhoneNumber());
                }
            }
            // drivingLicense
            if (updatedDriver.getDrivingLicense() != null) {
                if (!updatedDriver.getDrivingLicense().isEmpty()) {
                    validationDriver.setDrivingLicense(updatedDriver.getDrivingLicense());
                }
            }
            // licensedState
            if (updatedDriver.getLicensedState() != null) {
                if (!updatedDriver.getLicensedState().isEmpty()) {
                    validationDriver.setLicensedState(updatedDriver.getLicensedState());
                }
            }


            try{
                validationDriver.isValid();
            }catch (Exception e){
                session.stop();
                res.status(400);
                return dataToJson(e.getMessage());
            }

            //update value
            driver.setFirstName(validationDriver.getFirstName());
            driver.setLastName(validationDriver.getLastName());
            driver.setEmailAddress(validationDriver.getEmailAddress());
            driver.setPassword(validationDriver.getPassword());
            driver.setAddressLine1(validationDriver.getAddressLine1());
            driver.setAddressLine2(validationDriver.getAddressLine2());
            driver.setCity(validationDriver.getCity());
            driver.setState(validationDriver.getState());
            driver.setZip(validationDriver.getZip());
            driver.setPhoneNumber(validationDriver.getPhoneNumber());
            driver.setDrivingLicense(validationDriver.getDrivingLicense());
            driver.setLicensedState(validationDriver.getLicensedState());
            session.stop();
            res.type("application/json");
            return JsonUtil.dataToJson("Driver:" + req.params(":id") +" updated!");
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
