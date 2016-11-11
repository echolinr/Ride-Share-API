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

package com.team4.uberapp.persistence.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import com.team4.uberapp.car.Car;

@SuppressWarnings("UnusedDeclaration")
public class CarMapping extends AggregateMap<Car> {

    @Override
    public void map() {
        id().onProperty(element().getId()).natural();
        property().onField("make");
        property().onField("model");
        property().onField("license");
        property().onField("carType");
        property().onProperty(element().getMaxPassengers());
        property().onField("color");
        property().onField("validRideTypes");
        property().onField("driverId");
        //property().(element().getValidRideTypes().    );
        //property().onProperty(element().getCreationDate());
    }
}
