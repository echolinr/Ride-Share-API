/*
 * MongoLink, Object Document Mapper for Java and MongoDB
 *
 * Copyright (c) 2012, Arpinum or third-party contributors as
 * indicated by the @author tags
 *
 * MongoLink is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoLink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with MongoLink.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.team4.uberapp.car;

import com.team4.uberapp.domain.Validable;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Data
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

    public boolean isValid() throws Exception {
        //Could set up any additional validation rule
        if (this.make.isEmpty() || this.make.length() > 50) {
            throw new Exception("make at most 50 Characters");
        }
        if (this.license.isEmpty() || this.license.length() > 10) {
            throw new Exception("License Plate at most 10 Characters");
        }
        if (this.model.isEmpty() || this.model.length() > 50) {
            throw new Exception("Model at most 50 Characters");
        }
        if (this.carType.isEmpty() || this.carType.length() > 10) {
            throw new Exception("carType at most 10 Characters");
        }
        if (this.maxPassengers <=0 ) {
            throw new Exception("maxPassengers should greater than 0");
        }
        if (this.color.isEmpty() ||  this.color.length() > 10) {
            throw new Exception("color at most 10 Characters");
        }
        {
            final List<String> rideTypes = Arrays.asList("ECONOMY", "PREMIUM", "EXECUTIVE");
            if (!rideTypes.contains(this.validRideTypes)) {
                throw new Exception("not validRideTypes");
            }

        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
