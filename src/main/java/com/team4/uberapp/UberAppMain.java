package com.team4.uberapp;

import com.team4.uberapp.car.CarController;
import com.team4.uberapp.driver.DriverController;
import com.team4.uberapp.passenger.PassengerController;

import static spark.Spark.*;

/**
 * Created by lzhai & hectorguo on 2016/11/2.
 */
public class UberAppMain {
    public static void main(String[] args)  {
        String versionURI = "/v1";

        port(8080); /* service listening on port 8080 */

        /* http:a.b.c.d:8080/v1/ */
        get(versionURI +"/", (req, res) -> "Hello UberAPP!");

        // list all cars
        get(versionURI +"/cars", CarController.getAll);

        // get car by id : v1/cars/:ID
        get(versionURI +"/cars/:id", CarController.getById);

        /* post: /cars */
        post(versionURI + "/cars", CarController.create);

        // delete car by id : v1/cars/:ID
        delete(versionURI +"/cars/:id", CarController.delById);

        // patch car by id : v1/cars/:ID
        patch(versionURI +"/cars/:id", CarController.update);

        // Drivers
        get(versionURI +"/drivers", DriverController.getAll);
        get(versionURI +"/drivers/:id", DriverController.getById);
        post(versionURI + "/drivers", DriverController.create);
        delete(versionURI +"/drivers/:id", DriverController.delById);
        patch(versionURI +"/drivers/:id", DriverController.update);

        //Passengers
        get(versionURI +"/passengers", PassengerController.getAll);
        get(versionURI +"/passengers/:id", PassengerController.getById);
        post(versionURI+"/passengers", PassengerController.create);
        delete(versionURI+"/passengers/:id",PassengerController.delById);
        patch(versionURI +"/passengers/:id", PassengerController.update);


    }

/*
    public static void writeListToJsonArray( List list) throws IOException {
        OutputStream out = new ByteArrayOutputStream();

        JsonFactory jfactory = new JsonFactory();
        JsonGenerator jGenerator = jfactory.createJsonGenerator(out, JsonEncoding.UTF8);
        ObjectMapper mapper = new ObjectMapper();
        jGenerator.writeStartArray(); // [

        for (Event event : list) {
            String e = mapper.writeValueAsString(event);
            jGenerator.writeRaw(usage);
            // here, big hassles to write a comma to separate json objects, when the last object in the list is reached, no comma
        }

        jGenerator.writeEndArray(); // ]

        jGenerator.close();

        System.out.println(out.toString());
    }
 */
}
