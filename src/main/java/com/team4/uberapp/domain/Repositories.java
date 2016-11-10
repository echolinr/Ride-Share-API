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

package com.team4.uberapp.domain;;

public abstract class Repositories {

    public static void initialise(Repositories instance) {
        Repositories.instance = instance;
    }

    public static CarRepository cars() {
        return instance.carsRepository();
    }

    public static DriverRepository drivers() {
        return instance.driversRepository();
    }

    public static PassengerRepository passengers() {
        return instance.passengersRepository();
    }

    public static RideRepository rides() {
        return instance.ridesRepository();
    }

    protected abstract CarRepository carsRepository();
    protected abstract DriverRepository driversRepository();
    protected abstract PassengerRepository passengersRepository();
    protected abstract RideRepository ridesRepository();

    private static Repositories instance;
}
