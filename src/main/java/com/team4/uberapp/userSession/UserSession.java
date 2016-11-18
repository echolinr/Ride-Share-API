/**
 * UserSession for user authentication session
 *
 * @author  Lin Zhai
 * @version 0.1
 */

package com.team4.uberapp.userSession;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team4.uberapp.domain.Validable;
import com.team4.uberapp.util.ErrorReport;


//@Data
//@JsonIgnoreProperties( { "password","email" })
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

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
    @JsonIgnore
    public String getEmail() {
        return email;
    }
    @JsonProperty
    public void setEmail(String email) {
        this.email = email;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isValid() throws Exception {
        //Could set up any additional validation rule
        if (this.email.isEmpty() || this.email.length() > 50) {
            throw new Exception(ErrorReport.toJson(5001, "email at most 50 Characters"));
        }
        if (this.password.isEmpty() ) {
            throw new Exception(ErrorReport.toJson(5001, "Password is empty"));
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
