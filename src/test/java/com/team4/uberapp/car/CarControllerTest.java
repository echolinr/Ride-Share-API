package com.team4.uberapp.car;

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
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

/**
 * Created by lzhai on 2016/11/20.
 */
public class CarControllerTest {

    private static SparkTestUtil http;

    @Before
    public void setUp() throws Exception {
        // init http client connection
        http = new SparkTestUtil(8080);

        String [] args = new String[1];
        args[0] = "notoken";
        // init route
        UberAppMain.main(args);
        awaitInitialization();
    }

    @After
    public void tearDown() throws Exception {
        stop();
    }

    @Test
    public void canGetAllCars() {
        try {
            SparkTestUtil.UrlResponse response = http.doMethod("GET", "/v1/cars", null, "application/json");
            assertEquals(200,response.status);
        } catch (Exception e) {
            assertEquals(1,2);
        }
    }

    @Test
    public void canGetCarById() {
        // add a car in to db
        Car car = new Car("vw", "beetle", "5PVXXX", "Sedan", 4, "white", "ECONOMY");
        car.setId(UUID.randomUUID());
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        Repositories.cars().add(car);
        session.flush();

        // using GET/v1/car/:id
        String path = "/v1/cars/" + car.getId().toString();
        try {
            SparkTestUtil.UrlResponse response = http.doMethod("GET", path, null, "application/json");
            ObjectMapper mapper = new ObjectMapper();
            Car testCar = mapper.readValue(response.body, Car.class);

            //remove testing car from db
            //Repositories.cars().delete(car);
            session.stop();
            //test
            assertNotNull(testCar);
            assertEquals(car.getMake(), testCar.getMake());
            assertEquals(car.getModel(), testCar.getModel());
            assertEquals(car.getLicense(),testCar.getLicense());
            assertEquals(car.getCarType(), testCar.getCarType());
            assertEquals(car.getMaxPassengers(), testCar.getMaxPassengers());
            assertEquals(car.getColor(), testCar.getColor());
            assertEquals(car.getValidRideTypes(),testCar.getValidRideTypes());

        } catch (Exception e) {
            session.stop();
            assertEquals(1,2);
        }
    }

    @Test
    public void canDeleteCarById() {
        // add a car in to db
        Car car = new Car("vw", "beetle", "5PVXXX", "Sedan", 4, "white", "ECONOMY");
        car.setId(UUID.randomUUID());
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        Repositories.cars().add(car);
        session.stop();

        // using GET/v1/car/:id
        String path = "/v1/cars/" + car.getId().toString();
        try {
            SparkTestUtil.UrlResponse response = http.doMethod("DELETE", path, null, "application/json");
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Car testCar = Repositories.cars().get(car.getId());
            session.stop();
            assertNull(testCar);
            assertEquals(200, response.status);
        } catch (Exception e) {
            session.stop();
            assertEquals(1,2);
        }
    }

    @Test
    public void canPostCar() {
        //Car testCar = new Car("vw", "beetle", "5PVXXX", "Sedan", 4,"white", "ECONOMY");
        String reqestJson = "{" +
                "\"make\" : \"isuzu\","+
                "\"model\" : \"sx4\"," +
                "\"license\" : \"88PX123\"," +
                "\"carType\" : \"sedan\"," +
                "\"color\": \"blue\"," +
                "\"maxPassengers\" : 5,"+
                "\"validRideTypes\" : \"ECONOMY\"}";

        try {
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/cars",reqestJson, "application/json");
            ObjectMapper mapper = new ObjectMapper();
            Car testCar = mapper.readValue(response.body, Car.class);
            MongoSession session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Car dbCar  = Repositories.cars().get(testCar.getId());
           // session.flush();
            Repositories.cars().delete(dbCar);
            session.stop();
            assertNotNull(dbCar);
            assertEquals(testCar.getMake(), dbCar.getMake());
            assertEquals(testCar.getModel(), dbCar.getModel());
            assertEquals(testCar.getLicense(),dbCar.getLicense());
            assertEquals(testCar.getCarType(), dbCar.getCarType());
            assertEquals(testCar.getMaxPassengers(), dbCar.getMaxPassengers());
            assertEquals(testCar.getColor(), dbCar.getColor());
            assertEquals(testCar.getValidRideTypes(),dbCar.getValidRideTypes());
        } catch (Exception e) {
            assertEquals(1,2);
        }
    }

    @Test
    public void canNotPostCarWithInvalidRideTypes() {
        //Car testCar = new Car("vw", "beetle", "5PVXXX", "Sedan", 4,"white", "ECONOMY");
        String reqestJson = "{" +
                "\"make\" : \"isuzu\","+
                "\"model\" : \"sx4\"," +
                "\"license\" : \"88PX123\"," +
                "\"carType\" : \"sedan\"," +
                "\"color\": \"blue\"," +
                "\"maxPassengers\" : 5,"+
                "\"validRideTypes\" : \"ABC\"}";

        try {
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/cars",reqestJson, "application/json");
            assertEquals(400, response.status);
        } catch (Exception e) {
            assertEquals(1,2);
        }
    }

    @Test
    public void canNotPostCarWithInvalidMake() {
        String reqestJson = "{" +
                "\"make\" : \"isuzuafdafadfadshfaldjfaldkfhaldskhfaldshfaldskjfadlksfjadlfhadf\","+
                "\"model\" : \"sx4\"," +
                "\"license\" : \"88PX123\"," +
                "\"carType\" : \"sedan\"," +
                "\"color\": \"blue\"," +
                "\"maxPassengers\" : 5,"+
                "\"validRideTypes\" : \"ABC\"}";

        try {
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/cars",reqestJson, "application/json");
            assertEquals(400, response.status);
        } catch (Exception e) {
            assertEquals(1,2);
        }
    }

    @Test
    public void canNotPostCarWhenMissingProperty() {
        String reqestJson = "{" +
                "\"make\" : \"isuzuafdafadfadshfaldjfaldkfhaldskhfaldshfaldskjfadlksfjadlfhadf\","+
                "\"model\" : \"sx4\"," +
                "\"license\" : \"88PX123\"," +
                "\"carType\" : \"sedan\"," +
                //"\"color\": \"blue\"," +
                "\"maxPassengers\" : 5,"+
                "\"validRideTypes\" : \"ABC\"}";

        try {
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/cars",reqestJson, "application/json");
            assertEquals(400, response.status);
        } catch (Exception e) {
            assertEquals(1,2);
        }
    }
}