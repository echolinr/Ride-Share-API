package com.team4.uberapp;

import com.team4.uberapp.car.CarController;
import com.team4.uberapp.driver.DriverController;
import com.team4.uberapp.passenger.PassengerController;
import com.team4.uberapp.ride.RideController;

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


        // Ride sub-resouce car
        get(versionURI +"/drivers/:driverId/cars", CarController.getByDriverId);
        post(versionURI + "/drivers/:driverId/cars", CarController.createByDriverId);

        //Passengers
        get(versionURI +"/passengers", PassengerController.getAll);
        get(versionURI +"/passengers/:id", PassengerController.getById);
        post(versionURI+"/passengers", PassengerController.create);
        delete(versionURI+"/passengers/:id",PassengerController.delById);
        patch(versionURI +"/passengers/:id", PassengerController.update);

        // Rides
        get(versionURI +"/rides", RideController.getAll);
        get(versionURI +"/rides/:id", RideController.getById);
        post(versionURI + "/rides", RideController.create);
        delete(versionURI +"/rides/:id", RideController.delById);
        patch(versionURI +"/rides/:id", RideController.update);
    }

}
