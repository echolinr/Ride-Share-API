package com.team4.uberapp.userSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.UberAppMain;
import com.team4.uberapp.car.Car;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.passenger.Passenger;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.AppUser;
import com.team4.uberapp.util.SparkTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongolink.MongoSession;

import java.util.UUID;

import static com.team4.uberapp.util.UberAppUtil.hashPassword;
import static com.team4.uberapp.util.UberAppUtil.validTokenUser;
import static org.junit.Assert.*;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

/**
 * Created by lzhai on 2016/11/24.
 */



public class UserSessionTest {

    private static SparkTestUtil http;

    @Before
    public void setUp() throws Exception {
        // init http client connection
        http = new SparkTestUtil(8080);

        // init route
        UberAppMain.main(null);
        awaitInitialization();

    }

    @After
    public void tearDown() throws Exception {
        stop();
    }

    @Test
    public void canNotPostCarsBeforeLoginWithoutToken () {
        //post car without token
        String reqestJson = "{" +
                "\"make\" : \"isuzu\","+
                "\"model\" : \"sx4\"," +
                "\"license\" : \"88PX123\"," +
                "\"carType\" : \"sedan\"," +
                "\"color\": \"blue\"," +
                "\"maxPassengers\" : 5,"+
                "\"validRideTypes\" : \"ECONOMY\"}";

        try {
            //make a post
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/cars", reqestJson, "application/json");
            //should fail, because of no token
            assertEquals(401,response.status);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }

    }

    @Test
    public void canNotPostRidesBeforeLoginWithoutToken () {
        String reqestJson = "{" +
                "\"make\" : \"isuzu\","+
                "\"model\" : \"sx4\"," +
                "\"license\" : \"88PX123\"," +
                "\"carType\" : \"sedan\"," +
                "\"color\": \"blue\"," +
                "\"maxPassengers\" : 5,"+
                "\"validRideTypes\" : \"ECONOMY\"}";
        try {
            //make a post
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/rides", reqestJson, "application/json");
            //should fail, because of no token
            assertEquals(401,response.status);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }

    }

    @Test
    public void canNotPostCarsInDriverBeforeLoginWithoutToken () {
        String reqestJson = "{" +
                "\"make\" : \"isuzu\","+
                "\"model\" : \"sx4\"," +
                "\"license\" : \"88PX123\"," +
                "\"carType\" : \"sedan\"," +
                "\"color\": \"blue\"," +
                "\"maxPassengers\" : 5,"+
                "\"validRideTypes\" : \"ECONOMY\"}";
        try {
            //make a post
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/drivers/", reqestJson, "application/json");
            //should fail, because of no token
            assertEquals(401,response.status);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }

    }

    @Test
    public void canNotPostWithInvalidToken () {
        //post car without token
        String path = path ="/v1/cars?token=adfadfajbhoiadsjfadfa";
        String reqestJson = "{" +
                "\"make\" : \"isuzu\","+
                "\"model\" : \"sx4\"," +
                "\"license\" : \"88PX123\"," +
                "\"carType\" : \"sedan\"," +
                "\"color\": \"blue\"," +
                "\"maxPassengers\" : 5,"+
                "\"validRideTypes\" : \"ECONOMY\"}";

        try {
            //make a post
            SparkTestUtil.UrlResponse response = http.doMethod("POST", path, reqestJson, "application/json");
            //should fail, because of no token
            assertEquals(401,response.status);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }

    }


    @Test
    public void canLogInTokenContainUserId () {
        // add a passenger in to db
        Passenger passenger = new Passenger("Test", "Unit", "tunit@west.abc.com", "12345678", "Sillon Valley", null, "Sunnyvale", "CA",
                                   "94089", "123-456-7890");
        passenger.setId(UUID.randomUUID());
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        passenger.setPassword(hashPassword(passenger.getPassword()));
        Repositories.passengers().add(passenger);
        session.stop();

        // Login via passenger's email and password
        String path = "/v1/sessions";
        String reqestJson = "{" +
                "\"email\" : \"tunit@west.abc.com\","+
                "\"password\" : \"12345678\"}";
        try {
            //make a session post to login
            SparkTestUtil.UrlResponse response = http.doMethod("POST", path, reqestJson, "application/json");
            //get token from response
            ObjectMapper mapper = new ObjectMapper();
            SessionToken returnToken = mapper.readValue(response.body, SessionToken.class);
            // remove testing passenger data from db
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Repositories.passengers().get(passenger.getId());
            Repositories.passengers().delete(passenger);
            session.stop();

            //verify return response code
            assertEquals(201, response.status);
            AppUser appUser = validTokenUser(returnToken.getToken());
            assertNotNull(appUser);
            //make sure right user ID is in the token
            assertEquals(passenger.getId().toString(),appUser.getUserID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }
    @Test
    public void canNotLoginWithWrongUser () {
        // add a passenger in to db
        Passenger passenger = new Passenger("Test", "Unit", "tunit@west.abc.com", "12345678", "Sillon Valley", null, "Sunnyvale", "CA",
                "94089", "123-456-7890");
        passenger.setId(UUID.randomUUID());
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        passenger.setPassword(hashPassword(passenger.getPassword()));
        Repositories.passengers().add(passenger);
        session.stop();

        // Login via passenger's email and password, but email address is wrong
        String path = "/v1/sessions";
        String reqestJson = "{" +
                "\"email\" : \"tunit@abc.com\","+
                "\"password\" : \"12345678\"}";
        try {
            //login
            SparkTestUtil.UrlResponse response = http.doMethod("POST", path, reqestJson, "application/json");

            // remove testing  passenger data
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Repositories.passengers().get(passenger.getId());
            Repositories.passengers().delete(passenger);
            session.stop();

            //should failed because of wrong email addess
            assertEquals(401, response.status);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }
    @Test
    public void canPostCarAfterLogin () {
        // add a passenger in to db
        Passenger passenger = new Passenger("Test", "Unit", "tunit@west.abc.com", "12345678", "Sillon Valley", null, "Sunnyvale", "CA",
                "94089", "123-456-7890");
        passenger.setId(UUID.randomUUID());
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        passenger.setPassword(hashPassword(passenger.getPassword()));
        Repositories.passengers().add(passenger);
        session.stop();

        // Prepare login post
        String path = "/v1/sessions";
        String reqestJson = "{" +
                "\"email\" : \"tunit@west.abc.com\","+
                "\"password\" : \"12345678\"}";
        try {
            //login
            SparkTestUtil.UrlResponse response = http.doMethod("POST", path, reqestJson, "application/json");
            //get token
            ObjectMapper mapper = new ObjectMapper();
            SessionToken returnToken = mapper.readValue(response.body, SessionToken.class);

            //prepare to post a car, add token
            path ="/v1/cars?token=" + returnToken.getToken();
            reqestJson = "{" +
                    "\"make\" : \"isuzu\","+
                    "\"model\" : \"sx4\"," +
                    "\"license\" : \"88PX123\"," +
                    "\"carType\" : \"sedan\"," +
                    "\"color\": \"blue\"," +
                    "\"maxPassengers\" : 5,"+
                    "\"validRideTypes\" : \"ECONOMY\"}";

            //make a post
            response = http.doMethod("POST", path, reqestJson, "application/json");
            //get result of the post
            Car testCar = mapper.readValue(response.body, Car.class);

            // check db directly to see if the car is in
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Car dbCar =Repositories .cars().get(testCar.getId());

            //removing testing data, passenger & car
            Repositories.cars().delete(dbCar);
            passenger = Repositories.passengers().get(passenger.getId());
            Repositories.passengers().delete(passenger);
            session.stop();

            //verifying prepare car information and posted car in db are matching
            assertEquals(200, response.status);
            assertEquals(testCar.getId(),dbCar.getId());
            assertEquals(testCar.getMake(), dbCar.getMake());
            assertEquals(testCar.getModel(), dbCar.getModel());
            assertEquals(testCar.getLicense(),dbCar.getLicense());
            assertEquals(testCar.getCarType(), dbCar.getCarType());
            assertEquals(testCar.getMaxPassengers(), dbCar.getMaxPassengers());
            assertEquals(testCar.getColor(), dbCar.getColor());
            assertEquals(testCar.getValidRideTypes(),dbCar.getValidRideTypes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }
}