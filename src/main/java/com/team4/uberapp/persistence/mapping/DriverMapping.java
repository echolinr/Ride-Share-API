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

import com.team4.uberapp.driver.Driver;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class DriverMapping extends AggregateMap<Driver> {

    @Override
    public void map() {
        id().onProperty(element().getId()).natural();
        property().onField("firstName");
        property().onField("lastName");
        property().onField("emailAddress");
        property().onField("password");
        property().onField("addressLine1");
        property().onField("addressLine2");
        property().onField("city");
        property().onField("state");
        property().onField("zip");
        property().onField("phoneNumber");
        property().onField("drivingLicense");
        property().onField("licensedState");
    }
}
