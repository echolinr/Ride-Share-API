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
    private String validRideTypes;  // String Array Values are ECONOMY, PREMIUM, EXECUTIVE

    @SuppressWarnings("UnusedDeclaration")
    protected Car() {
        // for mongolink
    }

    public Car(String make, String model, String license, String carType, int maxPassengers, String validRideTypes) {
        this.id = UUID.randomUUID();
        this.make   = make;
        this.model  =  model;
        this.license    = license;
        this.carType    = carType;
        this.maxPassengers  = maxPassengers;
        this.validRideTypes = validRideTypes;
        System.out.println( make+ model + license+ carType+ maxPassengers+validRideTypes);
    }

    public boolean isValid() throws Exception{
        //Could set up any additional validation rule
        if (this.license.length() < 8){
            throw new Exception("The License Plate Should Have At Least 8 Characters");
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
