package com.team4.uberapp.ride;

/**
 * Created by HectorGuo on 11/8/16.
 */

import com.team4.uberapp.domain.Validable;
import com.team4.uberapp.util.ErrorReport;
import lombok.Data;
import com.team4.uberapp.location.Coordinate;

import java.util.UUID;
import java.sql.Timestamp;

@Data
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