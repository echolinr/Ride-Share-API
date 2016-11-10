package com.team4.uberapp.ride;

/**
 * Created by HectorGuo on 11/8/16.
 */

import com.team4.uberapp.domain.Validable;
import lombok.Data;

import java.util.UUID;

@Data
public class Ride implements Validable {
    private UUID id;
    private String name;

    @SuppressWarnings("UnusedDeclaration")
    protected Ride() {
        // for mongolink
    }

    public Ride(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        System.out.println( name );
    }

    public boolean isValid() throws Exception{
        //Could set up any additional validation rule
        if (this.name.isEmpty()){
            throw new Exception("The Ride name should not be empty");
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}