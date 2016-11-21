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

import com.team4.uberapp.car.Car;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.test.WithRepository;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

public class CarMongoRepositoryTest {

    @Rule
    public WithRepository withRepository = new WithRepository();

    @Test
    public void canAdd() {
        Car car = new Car("vw", "beetle", "5PVXXX", "Sedan", 4,"white", "ECONOMY");
        car.setId(UUID.randomUUID());
        Repositories.cars().add(car);
        withRepository.cleanSession();

        Car carFound = Repositories.cars().get(car.getId());

        assertNotNull(carFound);
        assertEquals(carFound.getMake(),car.getMake());
        assertNotNull(carFound.getId());
        assertEquals(carFound.getModel(),car.getModel());
        assertEquals(carFound.getLicense(),car.getLicense());
        assertEquals(carFound.getMaxPassengers(),car.getMaxPassengers());
        assertEquals(carFound.getColor(),car.getColor());
        assertEquals(carFound.getValidRideTypes(),car.getValidRideTypes());
    }

    @Test
    public void canDelete() {
        Car car = new Car("toyota", "camery", "7WZXXX", "Sedan", 4, "white", "PREMIUM" );
        car.setId(UUID.randomUUID());
        Repositories.cars().add(car);

        Repositories.cars().delete(car);
        withRepository.cleanSession();

        assertNull(Repositories.cars().get(car.getId()));
    }

    @Test
    public void canGetAll() {
        Car car1 = new Car("vw", "beetle", "5PVXXX", "Sedan", 4, "white", "ECONOMY");
        car1.setId(UUID.randomUUID());
        Car car2 = new Car("toyota", "camery", "7WZXXX", "Sedan", 4,"white",  "PREMIUM");
        car2.setId(UUID.randomUUID());
        int carSize;
        Repositories.cars().add(car1);
        Repositories.cars().add(car2);
        withRepository.cleanSession();

        List<Car> cars = Repositories.cars().all();
        carSize = cars.size();
        Repositories.cars().delete(car1);
        Repositories.cars().delete(car2);
        assertEquals(carSize,2);
    }

}
