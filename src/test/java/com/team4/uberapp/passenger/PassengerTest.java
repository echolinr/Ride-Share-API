package com.team4.uberapp.passenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.UberAppMain;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.SparkTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongolink.MongoSession;

import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

/**
 * Created by lzhai on 2016/11/24.
 */
public class PassengerTest {
    private static SparkTestUtil http;

    @Before
    public void setUp() throws Exception {
        // init http client connection
        http = new SparkTestUtil(8080);

        String [] args = new String[1];
        args[0] = "notoken";
        // init route, but skip token on purpose. authenticaiton  part will be handled separately
        UberAppMain.main(args);
        awaitInitialization();
    }

    @After
    public void tearDown() throws Exception {
        stop();
    }
    @Test
    public void canGetAllPassengers() {
        try {
            SparkTestUtil.UrlResponse response = http.doMethod("GET", "/v1/passengers", null, "application/json");
            // make sure response is right
            assertEquals(200,response.status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void canGetPassengerById() {
        // add a test passenger in to db directly
        Passenger passenger = new Passenger(
                "nala","zhai", "nala@gmail.com","password","stevens creek","apt 407","cupertino","ca","95014","650-525-2525"
        );
        passenger.setId(UUID.randomUUID());
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        Repositories.passengers().add(passenger);
        session.stop();


        // preparing GET/v1/passenger/:id
        String path = "/v1/passengers/" + passenger.getId().toString();
        try {
            SparkTestUtil.UrlResponse response = http.doMethod("GET", path, null, "application/json");
            // get passenger from response
            ObjectMapper mapper = new ObjectMapper();
            Passenger testPassenger = mapper.readValue(response.body, Passenger.class);

            // remove testing passenger from db
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            // need load passenger in before delete, a trick
            Repositories.passengers().get(testPassenger.getId());
            Repositories.passengers().delete(passenger);
            session.stop();

            //verify information match
            assertNotNull(testPassenger);
            assertEquals(200,response.status);
            assertEquals(passenger.getFirstName(), testPassenger.getFirstName());
            assertEquals(passenger.getLastName(), testPassenger.getLastName());
            assertEquals(passenger.getEmailAddress(),testPassenger.getEmailAddress());
            assertEquals(passenger.getAddressLine1(),testPassenger.getAddressLine1());
            assertEquals(passenger.getAddressLine2(), testPassenger.getAddressLine2());
            assertEquals(passenger.getCity(),testPassenger.getCity());
            assertEquals(passenger.getState(),testPassenger.getState());
            assertEquals(passenger.getZip(),testPassenger.getZip());
            assertEquals(passenger.getPhoneNumber(),testPassenger.getPhoneNumber());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void canDeletePassengerById() {
        // add a passenger into db
        Passenger passenger = new Passenger("nala", "zhai", "nala@gmail.com", "password", "20350 stevens creek", "apt407", "cupertino", "ca", "95014","852-545-4215");
        passenger.setId(UUID.randomUUID());
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        Repositories.passengers().add(passenger);
        session.stop();

        // using DELETE/v1/passenger/:id
        String path = "/v1/passengers/" + passenger.getId().toString();
        try {
            SparkTestUtil.UrlResponse response = http.doMethod("DELETE", path, null, "application/json");
            // check db, car should be removed already
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Passenger testPassenger = Repositories.passengers().get(passenger.getId());
            session.stop();
            // testPassenger should be null,
            assertNull(testPassenger);
            assertEquals(200, response.status);
        } catch (Exception e) {
            session.stop();
            assertTrue(false);
        }
    }

    @Test
    public void canPostPassenger() {
        // prepare json
        String reqestJson = "{" +
                "\"firstName\" : \"nala\","+
                "\"lastName\" : \"zhai\"," +
                "\"emailAddress\" : \"nala@gmail.com\"," +
                "\"password\" : \"password\"," +
                "\"addressLine1\": \"20350 stevens creek\"," +
                "\"addressLine2\" : \"apt 407\"," +
                "\"city\" : \"cupertino\","+
                "\"state\" : \"ca\","+
                "\"zip\" : \"85245\","+
                "\"phoneNumber\" : \"650-254-2544\"}";

        try {
            //make a post
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/passengers",reqestJson, "application/json");

            // verify http response status
            assertEquals(201,response.status);

            //get result of the post
            ObjectMapper mapper = new ObjectMapper();
            Passenger testPassenger = mapper.readValue(response.body, Passenger.class);

            //check db
            MongoSession session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Passenger dbPassenger  = Repositories.passengers().get(testPassenger.getId());
            // session.flush();
            // removing testing data in db
            Repositories.passengers().delete(dbPassenger);
            session.stop();

            // verify data mathced
            assertNotNull(dbPassenger);
            assertEquals(testPassenger.getFirstName(), dbPassenger.getFirstName());
            assertEquals(testPassenger.getLastName(), dbPassenger.getLastName());
            assertEquals(testPassenger.getEmailAddress(), dbPassenger.getEmailAddress());
//            assertEquals(testPassenger.getPassword(), dbPassenger.getPassword());
            assertEquals(testPassenger.getAddressLine1(), dbPassenger.getAddressLine1());
            assertEquals(testPassenger.getAddressLine2(), dbPassenger.getAddressLine2());
            assertEquals(testPassenger.getCity(), dbPassenger.getCity());
            assertEquals(testPassenger.getState(), dbPassenger.getState());
            assertEquals(testPassenger.getZip(), dbPassenger.getZip());
            assertEquals(testPassenger.getPhoneNumber(), dbPassenger.getPhoneNumber());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void canNotPostPassengerWithInvalidEmail() {
        //prepare json, but emailAddress is illegal
        String reqestJson = "{" +
                "\"firstName\" : \"nala\","+
                "\"lastName\" : \"zhai\"," +
                "\"emailAddress\" : \"xxx\"," +
                "\"password\" : \"password\"," +
                "\"addressLine1\": \"20350 stevens creek\"," +
                "\"addressLine2\" : \"apt 407\"," +
                "\"city\" : \"cupertino\","+
                "\"state\" : \"ca\","+
                "\"zip\" : \"85245\","+
                "\"phoneNumber\" : \"650-254-2544\"}";

        try {
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/passengers",reqestJson, "application/json");
            // should fail, with response.status 400
            assertEquals(400, response.status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void canNotPostPassengerWhenMissingProperty() {
        //prepare json, but missing last name
        String reqestJson = "{" +
                "\"firstName\" : \"nala\","+
                //"\"lastName\" : \"zhai\"," +
                "\"emailAddress\" : \"nala@gmail.com\"," +
                "\"password\" : \"password\"," +
                "\"addressLine1\": \"20350 stevens creek\"," +
                "\"addressLine2\" : \"apt 407\"," +
                "\"city\" : \"cupertino\","+
                "\"state\" : \"ca\","+
                "\"zip\" : \"85245\","+
                "\"phoneNumber\" : \"650-254-2544\"}";

        try {
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/passengers",reqestJson, "application/json");
            assertEquals(400, response.status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void canPatchPassenger() {
        // add a passenger in to db
        Passenger passenger = new Passenger("nala", "zhai", "nala@gmail.com", "password", "20350 stevens creek", "apt407", "cupertino", "ca", "95014","852-545-4215");
        passenger.setId(UUID.randomUUID());

        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        Repositories.passengers().add(passenger);
        session.stop();

        // using PATCH/v1/passenger/:id
        String path = "/v1/passengers/" + passenger.getId().toString();
        String reqestJson = "{" +
                "\"firstName\" : \"yaya\","+
                "\"emailAddress\" : \"yaya@gmail.com\"}";
        try {
            // patch a passenger
            SparkTestUtil.UrlResponse response = http.doMethod("PATCH", path, reqestJson, "application/json");
            // get passenger from db
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Passenger testPassenger = Repositories.passengers().get(passenger.getId());
            //Repositories.passengers().delete(testPassenger);
            session.stop();
            //verify information is modified
            assertNotNull(testPassenger);
            assertEquals(200, response.status);
            assertEquals("yaya", testPassenger.getFirstName());
            assertEquals("yaya@gmail.com",testPassenger.getEmailAddress());
        } catch (Exception e) {
            assertTrue(false);
        }
    }



}