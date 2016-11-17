package com.team4.uberapp;

import com.team4.uberapp.car.CarController;
import com.team4.uberapp.driver.DriverController;
import com.team4.uberapp.passenger.PassengerController;
import com.team4.uberapp.ride.RideController;
import com.team4.uberapp.userSession.UserSessionController;

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

        //add access control for later
        /*
        before((request,response)->{
            String method = request.requestMethod();
            if (method.equals("POST")) {
                if (request.pathInfo().equals(versionURI+ "/rides") || request.pathInfo().equals(versionURI+"/cars")) {
                    halt(401,"User unathorized");
                }
            }
        });
        */

        // Cars
        get(versionURI +"/cars", CarController.getAll);        // get all cars: v1/cars
        get(versionURI +"/cars/:id", CarController.getById); // get car by id : v1/cars/:id
        post(versionURI + "/cars", CarController.create);   // post  /cars
        delete(versionURI +"/cars/:id", CarController.delById); // delete car by id: v1/cars/:id
        patch(versionURI +"/cars/:id", CarController.update); // patch car by id : v1/cars/:id

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


        // Rides' route points
        get(versionURI + "/rides/:id/routePoints", RideController.getRoutePoints);
        post(versionURI + "/rides/:id/routePoints", RideController.addRoutePoints);

        // User session
        get(versionURI +"/sessions", UserSessionController.getAll);        // get all cars: v1/cars
        post(versionURI + "/sessions", UserSessionController.create);   // post  /cars
//        get(versionURI +"/sessions/:id", UserSessionController.getById); // get car by id : v1/cars/:id
//        delete(versionURI +"/sessions/:id", UserSessionController.delById); // delete car by id: v1/cars/:id
    }

}
