package com.team4.uberapp.ride;

/**
 * Created by HectorGuo on 11/8/16.
 */

import com.team4.uberapp.domain.Validable;
import com.team4.uberapp.location.Coordinate;

import java.sql.Timestamp;
import java.util.UUID;

//@Data
public class RoutePoint implements Validable {
    private UUID id;
    private Coordinate point;
    private Timestamp timestamp;
    private UUID rideId;



    @SuppressWarnings("UnusedDeclaration")
    protected RoutePoint() {
        // for mongolink
    }

    public RoutePoint(float lat, float lng, Timestamp timestamp, UUID rideId) {
        this.id = UUID.randomUUID();
        this.point = new Coordinate(lat, lng);
        this.timestamp = timestamp;
        this.rideId = rideId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Coordinate getPoint() {
        return point;
    }

    public void setPoint(Coordinate point) {
        this.point = point;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getRideId() {
        return rideId;
    }

    public void setRideId(UUID rideId) {
        this.rideId = rideId;
    }

    public boolean isValid() throws Exception{
        //Could set up any additional validation rule
        if (this.timestamp.toString().isEmpty()){
            throw new Exception("timestamp should not be empty");
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}