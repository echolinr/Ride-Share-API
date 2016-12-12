/**
 * Car Class defines the object format for car in MongoDB
 *
 * @author  Lin Zhai  & Hector Guo
 * @version 0.2
 */
package com.team4.uberapp.car;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.team4.uberapp.domain.Validable;
import com.team4.uberapp.util.ErrorReport;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


//@Data
@JsonIgnoreProperties({"valid"})
public class Car implements Validable, Cloneable {
    private UUID id;
    //private DateTime creationDate = new DateTime();
    private String make;
    private String model;
    private String license;
    private String carType;
    private int maxPassengers;
    private String color;
    private String validRideTypes;  // String Array Values are ECONOMY, PREMIUM, EXECUTIVE
    private UUID driverId;

    @SuppressWarnings("UnusedDeclaration")
    protected Car() {
        // for mongolink
    }

    /**
     * Instantiates a new Car.
     *
     * @param make           the make
     * @param model          the model
     * @param license        the license
     * @param carType        the car type
     * @param maxPassengers  the max passengers
     * @param color          the color
     * @param validRideTypes the valid ride types
     */
    public Car(String make, String model, String license, String carType, int maxPassengers, String color, String validRideTypes) {
        this.id = UUID.randomUUID();
        this.make   = make;
        this.model  =  model;
        this.license    = license;
        this.carType    = carType;
        this.maxPassengers  = maxPassengers;
        this.color = color;
        this.validRideTypes = validRideTypes;
        System.out.println( make+ model + license+ carType+ maxPassengers+validRideTypes);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getValidRideTypes() {
        return validRideTypes;
    }

    public void setValidRideTypes(String validRideTypes) {
        this.validRideTypes = validRideTypes;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public boolean isValid() throws Exception {
        //Could set up any additional validation rule
        if (this.make == null || this.make.isEmpty() || this.make.length() > 50) {
            throw new Exception(ErrorReport.toJson(2001, "make at most 50 Characters"));
        }
        if (this.license == null || this.license.isEmpty() || this.license.length() > 10) {
            throw new Exception(ErrorReport.toJson(2001, "License Plate at most 10 Characters"));
        }
        if (this.model == null || this.model.isEmpty() || this.model.length() > 50) {
            throw new Exception(ErrorReport.toJson(2001, "Model at most 50 Characters"));
        }
        if (this.carType == null || this.carType.isEmpty() || this.carType.length() > 10) {
            throw new Exception(ErrorReport.toJson(2001, "carType at most 10 Characters"));
        }
        if (this.maxPassengers <=0 ) {
            throw new Exception(ErrorReport.toJson(2001, "maxPassengers should greater than 0"));
        }
        if (this.color == null || this.color.isEmpty() ||  this.color.length() > 10) {
            throw new Exception(ErrorReport.toJson(2001, "color at most 10 Characters"));
        }
        {
            final List<String> rideTypes = Arrays.asList("ECONOMY", "PREMIUM", "EXECUTIVE");
            if (!rideTypes.contains(this.validRideTypes)) {
                throw new Exception(ErrorReport.toJson(2001, "not validRideTypes"));
            }

        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
