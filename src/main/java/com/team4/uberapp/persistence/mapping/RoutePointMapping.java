/**
 * Route Point Mapping
 */

package com.team4.uberapp.persistence.mapping;

import com.team4.uberapp.ride.RoutePoint;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class RoutePointMapping extends AggregateMap<RoutePoint> {

    @Override
    public void map() {
        id().onProperty(element().getId()).natural();
        property().onField("timestamp");
        property().onField("point");
//        property().onProperty(element().getPoint());
        //property().(element().getValidRideTypes().    );
        //property().onProperty(element().getCreationDate());
    }
}
