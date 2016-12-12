package com.team4.uberapp.ride;

/**
 * Created by HectorGuo on 11/8/16.
 */

import com.team4.uberapp.domain.Validable;

import java.util.UUID;

//@Data
public class RoutePoint implements Validable {
    private UUID id;
    private Number lat;
    private Number lng;
    private Number timestamp;
    private UUID rideId;



    @SuppressWarnings("UnusedDeclaration")
    protected RoutePoint() {
        // for mongolink
    }

    /**
     * Instantiates a new Route point.
     *
     * @param lat       the lat
     * @param lng       the lng
     * @param timestamp the timestamp
     * @param rideId    the ride id
     */
    public RoutePoint(float lat, float lng, Number timestamp, UUID rideId) {
        this.id = UUID.randomUUID();
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
        this.rideId = rideId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Number getLat() {
        return lat;
    }

    public void setLat(Number lat) {
        this.lat = lat;
    }

    public Number getLng() {
        return lng;
    }

    public void setLng(Number lng) {
        this.lng = lng;
    }

    public Number getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Number timestamp) {
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