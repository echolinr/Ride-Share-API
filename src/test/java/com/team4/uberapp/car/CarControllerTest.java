package com.team4.uberapp.car;

import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
        session.stop();


        // using GET/v1/car/:id
        String path = "/v1/cars/" + car.getId().toString();
        try {
            SparkTestUtil.UrlResponse response = http.doMethod("GET", path, null, "application/json");
            ObjectMapper mapper = new ObjectMapper();
            Car testCar = mapper.readValue(response.body, Car.class);

            // remove testing car from db
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            // need load car in before delete
            Repositories.cars().get(testCar.getId());
            Repositories.cars().delete(car);
            session.stop();

            //test
            assertNotNull(testCar);
            assertEquals(200,response.status);
            assertEquals(car.getMake(), testCar.getMake());
            assertEquals(car.getModel(), testCar.getModel());
            assertEquals(car.getLicense(),testCar.getLicense());
            assertEquals(car.getCarType(), testCar.getCarType());
            assertEquals(car.getMaxPassengers(), testCar.getMaxPassengers());
            assertEquals(car.getColor(), testCar.getColor());
            assertEquals(car.getValidRideTypes(),testCar.getValidRideTypes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
                "\"make\" : \"isuzuafdafadfadshfa\","+
                "\"model\" : \"sx4\"," +
                "\"license\" : \"88PX123\"," +
                "\"carType\" : \"sedan\"," +
                //"\"color\": \"blue\"," +
                "\"maxPassengers\" : 5,"+
                "\"validRideTypes\" : \"ECONOMY\"}";

        try {
            SparkTestUtil.UrlResponse response = http.doMethod("POST", "/v1/cars",reqestJson, "application/json");
            assertEquals(400, response.status);
        } catch (Exception e) {
            assertEquals(1,2);
        }
    }
    @Test
    public void canPatchCar() {
        // add a car in to db
        Car car = new Car("vw", "audi", "5PVXXX", "Sedan", 4, "white", "ECONOMY");
        car.setId(UUID.randomUUID());
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        Repositories.cars().add(car);
        session.stop();

        // using PATCH/v1/car/:id
        String path = "/v1/cars/" + car.getId().toString();
        String reqestJson = "{" +
                "\"make\" : \"toyota\","+
                "\"validRideTypes\" : \"PREMIUM\"}";
        try {
            SparkTestUtil.UrlResponse response = http.doMethod("PATCH", path, reqestJson, "application/json");
            session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));
            Car testCar = Repositories.cars().get(car.getId());
            Repositories.cars().delete(testCar);
            session.stop();
            assertNotNull(testCar);
            assertEquals(200, response.status);
            assertEquals("toyota", testCar.getMake());
            assertEquals("PREMIUM",testCar.getValidRideTypes());
        } catch (Exception e) {
            assertEquals(1,2);
        }
    }
    @Test
    public void testQueryCount() {
        // using get/v1/car?count=xx
        int count = 1;
        String path = "/v1/cars?count=" + count;
        try {
            SparkTestUtil.UrlResponse response = http.doMethod("GET", path, null, "application/json");
            // convert json to list of cars
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<Car>> mapType = new TypeReference<List<Car>>() {};
            List<Car> jsonToCarList =  objectMapper.readValue(response.body, mapType);

            assertEquals(200,response.status);
            assertEquals(count,jsonToCarList.size());
        } catch (Exception e) {
            System.out.print(e.getMessage());
            assertEquals(1,2);
        }
    }
    @Test
    public void testQueryOffsetIdAndCount() {
        // using get/v1/car?count=xx
        int count = 1;
        int offsetId=0;
        int total = offsetId + count;
        String path = "/v1/cars?count=" + total;
        List<Car> totalCars;
        List<Car> indexCars;
        try {
            // get total cars first
            SparkTestUtil.UrlResponse response = http.doMethod("GET", path, null, "application/json");
            // convert json to list of cars
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<Car>> mapType = new TypeReference<List<Car>>() {};
            totalCars =  objectMapper.readValue(response.body, mapType);

            // get index cars
            path = "/v1/cars?offsetId=" + offsetId + "&count=" + count;
            response = http.doMethod("GET", path, null, "application/json");
            // convert json to list of cars
            indexCars =  objectMapper.readValue(response.body, mapType);

            assertEquals(200,response.status);
            assertEquals(count,indexCars.size());
            for (int idx = 0; idx<count; idx++) {
                Car expectCar = totalCars.get(idx+offsetId);
                Car acturalCar = indexCars.get(idx);
                assertEquals(expectCar.getId(),acturalCar.getId());
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
            assertEquals(1,2);
        }
    }
    @Test
    public void testQuerySortAndSortorder() {
        // using get/v1/car?count=xx
        //String path = "/v1/cars?offsetId=0&count=2&sort=maxPassengers&sortOrder=desc";
        String path = "/v1/cars?sort=maxPassengers&sortOrder=desc";
        try {
            // get total cars first
            SparkTestUtil.UrlResponse response = http.doMethod("GET", path, null, "application/json");
            // convert json to list of cars
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<Car>> mapType = new TypeReference<List<Car>>() {};
            List<Car> cars =  objectMapper.readValue(response.body, mapType);
            Car car0, car1;

            assertEquals(200,response.status);
            assertTrue(cars.size()>=2);
            //car0 = cars.get(0);
            //car1 = cars.get(1);
            //assertTrue(car0.getMaxPassengers()>= car1.getMaxPassengers());
            for (int idx=0; idx<cars.size()-1; idx++) {
                car0 = cars.get(idx);
                car1 = cars.get(idx+1);
                assertTrue(car0.getMaxPassengers()>=car1.getMaxPassengers());
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
            assertEquals(1,2);
        }

    }

}