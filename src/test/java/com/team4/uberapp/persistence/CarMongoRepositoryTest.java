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

import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.car.Car;
import com.team4.uberapp.domain.Repositories;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongolink.MongoSession;

import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

//import org.mongolink.MongoSession;

public class CarMongoRepositoryTest {

    //@Rule
    MongoSession session;
    //public WithRepository withRepository = new WithRepository();

    @Before
    public void setUp() throws Exception {
        session  = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        System.out.println("==========Setup test===============");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("==========Stop test===============");
        session.stop();
    }

    @Test
    public void canAdd() {

        Car car = new Car("vw", "beetle", "5PVXXX", "Sedan", 4,"white", "ECONOMY");
        car.setId(UUID.randomUUID());
        Repositories.cars().add(car);
        session.flush();

        Car carFound = Repositories.cars().get(car.getId());
        Repositories.cars().delete(car);
        session.flush();

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
        //session.start();
        //Repositories.initialise(new MongoRepositories(session));

        Car car = new Car("toyota", "camery", "7WZXXX", "Sedan", 4, "white", "PREMIUM" );
        car.setId(UUID.randomUUID());
        Repositories.cars().add(car);
        session.flush();
        Repositories.cars().delete(car);
        session.flush();
        //withRepository.cleanSession();

        car = Repositories.cars().get(car.getId());

        assertNull(car);
    }

    @Test
    public void canGetAll() {
        //session.start();
        //Repositories.initialise(new MongoRepositories(session));

        Car car1 = new Car("vw", "beetle", "5PVXXX", "Sedan", 4, "white", "ECONOMY");
        car1.setId(UUID.randomUUID());
        Car car2 = new Car("toyota", "camery", "7WZXXX", "Sedan", 4,"white",  "PREMIUM");
        car2.setId(UUID.randomUUID());
        int carSize;

        List<Car> cars = Repositories.cars().all();
        carSize = cars.size();
        session.clear();
        Repositories.cars().add(car1);
        Repositories.cars().add(car2);
        session.flush();
        //withRepository.cleanSession();

        cars = Repositories.cars().all();
        carSize = cars.size() - carSize;
        Repositories.cars().delete(car1);
        Repositories.cars().delete(car2);
        assertEquals(carSize,2);
    }

}
