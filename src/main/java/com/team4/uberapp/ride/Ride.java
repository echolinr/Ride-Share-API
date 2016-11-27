package com.team4.uberapp.ride;

/**
 * Created by HectorGuo on 11/8/16.
 */

import com.team4.uberapp.domain.Validable;
import com.team4.uberapp.location.Coordinate;
import com.team4.uberapp.util.ErrorReport;

import java.util.UUID;

//@Data
public class Ride implements Validable {
    private UUID id;
    private String rideType;
    private Number startLat;
    private Number startLong;
    private Number endLat;
    private Number endLong;
//    private Coordinate startPoint;
//    private Coordinate endPoint;
    private Number requestTime;
    private Number pickupTime;
    private Number dropOffTime;
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

    public Number getStartLat() {return startLat; }
    public Number getStartLong() {return startLong; }
    public Number getEndLat() {return endLat; }
    public Number getEndLong() {return endLong; }
    public void setStartLat(Number startLat) {this.startLat = startLat;}
    public void setStartLong(Number startLong) {this.startLong = startLong;}
    public void setEndLat(Number endLat) {this.endLat = endLat;}
    public void setEndLong(Number endLong) {this.endLong = endLong;}
//    public Coordinate getStartPoint() {
//        return startPoint;
//    }
//
//    public void setStartPoint(Coordinate startPoint) {
//        this.startPoint = startPoint;
//    }
//
//    public Coordinate getEndPoint() {
//        return endPoint;
//    }
//
//    public void setEndPoint(Coordinate endPoint) {
//        this.endPoint = endPoint;
//    }

    public Number getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Number requestTime) {
        this.requestTime = requestTime;
    }

    public Number getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Number pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Number getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(Number dropOffTime) {
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
     * @param startLat    the start lat
     * @param startLong   the start long
     * @param endLat      the end lat
     * @param endLong     the end long
     * @param requestTime the request time
     * @param pickupTime  the pickup time
     * @param dropOffTime the drop off time
     * @param status      the status
     * @param fare        the fare
     * @param driverId    the driver id
     * @param carId       the car id
     * @param passengerId the passenger id
     */
    public Ride(String rideType, Number startLat, Number startLong, Number endLat, Number endLong, Number requestTime, Number pickupTime, Number dropOffTime, String status, int fare, UUID driverId, UUID carId, UUID passengerId) {
        this.id = UUID.randomUUID();
        this.rideType = rideType;
//        this.startPoint = startPoint;
//        this.endPoint = endPoint;
        this.startLat = startLat;
        this.startLong = startLong;
        this.endLat = endLat;
        this.endLong = endLong;
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
        if (!this.status.equals("REQUESTED") && !this.status.equals("AWAITING_DRIVER") && !this.status.equals("DRIVE_ASSIGNED") && !this.status.equals("IN_PROGRESS") && !this.status.equals("ARRIVED") && !this.status.equals("CLOSED")) {
            throw new Exception(ErrorReport.toJson(4001, "The Ride status should be REQUESTED, AWAITING_DRIVER, DRIVE_ASSIGNED, IN_PROGRESS, ARRIVED or CLOSED"));
        }
        if (!this.rideType.equals("ECONOMY") && !this.rideType.equals("PREMIUM") && !this.rideType.equals("EXECUTIVE")) {
            throw new Exception(ErrorReport.toJson(4001, "The rideType should be ECONOMY, PREMIUM or EXECUTIVE"));
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}