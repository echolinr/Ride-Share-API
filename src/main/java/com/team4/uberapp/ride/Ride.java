package com.team4.uberapp.ride;

/**
 * Created by HectorGuo on 11/8/16.
 */

import com.team4.uberapp.domain.Validable;
import com.team4.uberapp.location.Coordinate;
import com.team4.uberapp.util.ErrorReport;

import java.sql.Timestamp;
import java.util.UUID;

//@Data
public class Ride implements Validable {
    private UUID id;
    private String rideType;
    private Coordinate startPoint;
    private Coordinate endPoint;
    private Timestamp requestTime;
    private Timestamp pickupTime;
    private Timestamp dropOffTime;
    private String status;
    private int fare;
    private UUID driverId;
    private UUID carId;
    private UUID passengerId;


    @SuppressWarnings("UnusedDeclaration")
    protected Ride() {
        // for mongolink
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public Coordinate getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Coordinate startPoint) {
        this.startPoint = startPoint;
    }

    public Coordinate getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Coordinate endPoint) {
        this.endPoint = endPoint;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    public Timestamp getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Timestamp pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Timestamp getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(Timestamp dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    public UUID getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(UUID passengerId) {
        this.passengerId = passengerId;
    }

    /**
     * Instantiates a new Ride.
     *
     * @param rideType    the ride type
     * @param startPoint  the start point
     * @param endPoint    the end point
     * @param requestTime the request time
     * @param pickupTime  the pickup time
     * @param dropOffTime the drop off time
     * @param status      the status
     * @param fare        the fare
     * @param driverId    the driver id
     * @param carId       the car id
     * @param passengerId the passenger id
     */
    public Ride(String rideType, Coordinate startPoint, Coordinate endPoint, Timestamp requestTime, Timestamp pickupTime, Timestamp dropOffTime, String status, int fare, UUID driverId, UUID carId, UUID passengerId) {
        this.id = UUID.randomUUID();
        this.rideType = rideType;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.requestTime = requestTime;
        this.pickupTime = pickupTime;
        this.dropOffTime = dropOffTime;
        this.status = status;
        this.fare = fare;
        this.driverId = driverId;
        this.carId = carId;
        this.passengerId = passengerId;
    }

    public boolean isValid() throws Exception{
        //Could set up any additional validation rule
        if (this.rideType.isEmpty()){
            throw new Exception(ErrorReport.toJson(4001, "The Ride name should not be empty"));
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}