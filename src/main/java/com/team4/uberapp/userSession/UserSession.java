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

package com.team4.uberapp.userSession;

import com.team4.uberapp.domain.Validable;
import lombok.Data;


@Data
//@JsonIgnoreProperties( { "password" })
public class UserSession implements Validable, Cloneable {
    private Object id;
    //private DateTime creationDate = new DateTime();
    private String email;
    private String password;
    private String token;

    @SuppressWarnings("UnusedDeclaration")
    protected UserSession() {
        // for mongolink
    }

    public UserSession(String email, String password) {
        //this.id = UUID.randomUUID();
        this.email   = email;
        this.password  =  password;
        //generate token
    }

    public boolean isValid() throws Exception {
        //Could set up any additional validation rule
        if (this.email.isEmpty() || this.email.length() > 50) {
            throw new Exception("email at most 50 Characters");
        }
        if (this.password.isEmpty() ) {
            throw new Exception("Password is empty");
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
