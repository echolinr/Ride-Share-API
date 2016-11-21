package com.team4.uberapp.car;

import com.team4.uberapp.UberAppMain;
import com.team4.uberapp.util.SparkTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
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
        // init route
        UberAppMain.main(null);
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

}