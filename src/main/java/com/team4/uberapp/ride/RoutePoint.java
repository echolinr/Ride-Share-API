package com.team4.uberapp.ride;

/**
 * Created by HectorGuo on 11/8/16.
 */

import com.team4.uberapp.domain.Validable;
import com.team4.uberapp.location.Coordinate;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
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