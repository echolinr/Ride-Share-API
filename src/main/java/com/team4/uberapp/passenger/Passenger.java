package com.team4.uberapp.passenger;

import com.team4.uberapp.domain.Validable;
import com.team4.uberapp.util.ErrorReport;
import lombok.Data;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by lzhai on 2016/11/10.
 */
@Data
//@JsonIgnoreProperties( { "password" })
public class Passenger implements Validable, Cloneable {
    private UUID id;
    private String firstName; // maximum 50 characters
    private String lastName; // maximum 50 characters
    private String emailAddress; // will add valid format later
 //   @JsonIgnore
    private String password; // min 8, max 20 //
    private String addressLine1; // max 100
    private String addressLine2; // max 100
    private String city; // max 50
    private String state; // 2, min 2
    private String zip; //5, min 5
    private String phoneNumber; // xxx-xxx-xxxx; will add valid format later

    @SuppressWarnings("UnusedDeclaration")
    protected Passenger() {
        // for mongolink
    }
/*
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
*/
    public Passenger(String firstName, String lastName, String emailAddress, String password, String addressLine1, String addressLine2, String city, String state, String zip, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.addressLine2 = addressLine2;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
    }


    public boolean isValid() throws Exception{
        //valid firstName length
        if (this.firstName.isEmpty() || this.firstName.length() >50){
            throw new Exception(ErrorReport.toJson(3001, "The firstName length should not greater than 50 Characters"));
        }
        //valid lastName length
        if (this.lastName.isEmpty() || this.lastName.length() > 50) {
            throw new Exception(ErrorReport.toJson(3001, "The lastName length should not greater than 50 Characters"));
        }
        //valid addressLine1 length
        if (this.addressLine1.isEmpty() || this.addressLine1.length() >100){
            throw new Exception(ErrorReport.toJson(3001, "The firstName length should not greater than 50 Characters"));
        }
        //valid addressLine2 length, optional, could be empty
        if (this.addressLine2.length() > 100) {
            throw new Exception(ErrorReport.toJson(3001, "The lastName length should not greater than 50 Characters"));
        }
        //valid passowrd
        //if (this.password.isEmpty() || this.password.length()<8 || this.password.length()>20) {
        //    throw new Exception(ErrorReport.toJson(3001, "The password length should >8 & <20"));
       // }
        //city
        if (this.city.isEmpty() || this.city.length() > 50) {
            throw new Exception(ErrorReport.toJson(3001, "The city length should not greater than 50 Characters"));
        }
        //state
        if (this.state.length() != 2) {
            throw new Exception(ErrorReport.toJson(3001, "The state length must be 2"));
        }
        //zip
        if (this.zip.length()!=5) {
            throw new Exception(ErrorReport.toJson(3001, "The zip length must be 2"));
        }
        //emailAddress

        {
            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
           if (pattern.matcher(this.emailAddress).matches() == false) {
               throw new Exception(ErrorReport.toJson(3001, "Invalid emailAddress"));
            }
        }

        //phone#
        {
            Pattern pattern = Pattern.compile("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}");
            if (pattern.matcher(this.phoneNumber).matches() == false) {
                throw new Exception(ErrorReport.toJson(3001, "Invalid phone #"));
            }
        }

        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
