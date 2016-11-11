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
import org.junit.Rule;
import org.junit.Test;
import com.team4.uberapp.domain.*;
import com.team4.uberapp.test.WithRepository;

import java.util.List;

import static org.fest.assertions.Assertions.*;

public class CarMongoRepositoryTest {

    @Rule
    public WithRepository withRepository = new WithRepository();

    @Test
    public void canAdd() {
        Car car = new Car("vw", "beetle", "5PVXXX", "Sedan", 4,"white", "ECONOMY");
        Repositories.cars().add(car);
        withRepository.cleanSession();

        Car carFound = Repositories.cars().get(car.getId());

        assertThat(carFound).isNotNull();
        assertThat(carFound.getMake()).isEqualTo("vw");
        assertThat(carFound.getId()).isNotNull();
        assertThat(carFound.getModel()).isEqualTo("beetle");
        assertThat(carFound.getLicense()).isEqualTo("5PVXXX");
        assertThat(carFound.getMaxPassengers() == 4);
        assertThat(carFound.getColor()).isEqualTo("white");
        assertThat(carFound.getValidRideTypes()).isEqualTo("ECONOMY");
    }

    @Test
    public void canDelete() {
        Car car = new Car("toyota", "camery", "7WZXXX", "Sedan", 4, "white", "PREMIUM" );
        Repositories.cars().add(car);

        Repositories.cars().delete(car);
        withRepository.cleanSession();

        assertThat(Repositories.cars().get(car.getId())).isNull();
    }

    @Test
    public void canGetAll() {
        Repositories.cars().add( new Car("vw", "beetle", "5PVXXX", "Sedan", 4, "white", "ECONOMY"));
        Repositories.cars().add(new Car("toyota", "camery", "7WZXXX", "Sedan", 4,"white",  "PREMIUM"));
        withRepository.cleanSession();

        List<Car> cars = Repositories.cars().all();

        assertThat(cars).hasSize(2);
    }

}
