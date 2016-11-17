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

package com.team4.uberapp.persistence;

import com.team4.uberapp.domain.*;
import org.mongolink.MongoSession;

public class MongoRepositories extends Repositories {

    public MongoRepositories(MongoSession session) {
        this.session = session;
    }

    @Override
    protected CarRepository carsRepository() {
        return new CarMongoRepository(session);
    }

    @Override
    protected DriverRepository driversRepository() {
        return new DriverMongoRepository(session);
    }

    @Override
    protected PassengerRepository passengersRepository() {
        return new PassengerMongoRepository(session);
    }

    @Override
    protected RideRepository ridesRepository() {
        return new RideMongoRepository(session);
    }

    @Override
    protected UserSessionRepository userSessionsRepository() {
        return new UserSessionMongoRepository(session);
    }
//
    @Override
    protected RoutePointRepository routePointsRepository() {
        return new RoutePointMongoRepository(session);
    }

    private MongoSession session;


}
