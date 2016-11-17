/**
 * Rides's sub resouce: route points
 */

package com.team4.uberapp.persistence;

import com.team4.uberapp.domain.RoutePointRepository;
import com.team4.uberapp.ride.RoutePoint;
import org.mongolink.MongoSession;

public class RoutePointMongoRepository extends MongoRepository<RoutePoint> implements RoutePointRepository {
    public RoutePointMongoRepository(MongoSession mongoSession) {
        super(mongoSession);
    }

}
