package com.team4.uberapp.userSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.UberAppMain;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
            assertEquals(401,response.status);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }

    }
    @Test
    public void canLogInWithValidPassengerEmailPassword () {
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

        // using GET/v1/car/:id
        String path = "/v1/sessions";
        String reqestJson = "{" +
                "\"email\" : \"tunit@west.abc.com\","+
                "\"password\" : \"12345678\"}";
        try {
            //make a post
            SparkTestUtil.UrlResponse response = http.doMethod("POST", path, reqestJson, "application/json");
            //get result of the post
            ObjectMapper mapper = new ObjectMapper();
            SessionToken returnToken = mapper.readValue(response.body, SessionToken.class);
            // remove testing data
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Repositories.passengers().get(passenger.getId());
            Repositories.passengers().delete(passenger);
            session.stop();

            //verify value
            assertEquals(201, response.status);
            AppUser appUser = validTokenUser(returnToken.getToken());
            assertNotNull(appUser);
            assertEquals(passenger.getId().toString(),appUser.getUserID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }

    }
}