package com.team4.uberapp.driver;

/**
 * Created by HectorGuo on 11/8/16.
 */

import com.team4.uberapp.domain.Validable;
import lombok.*;

import java.util.UUID;
import java.util.regex.Pattern;

@Data
public class Driver implements Validable {
    private UUID id;
    private String name;
    private String firstName; // maximum 50 characters
    private String lastName; // maximum 50 characters
    private String emailAddress; // will add valid format later
    private String password; // min 8, max 20 //
    private String addressLine1; // max 100
    private String addressLine2; // max 100
    private String city; // max 50
    private String state; // 2, min 2
    private String zip; //5, min 5
    private String phoneNumber; // xxx-xxx-xxxx; will add valid format later
    private String drivingLicense; // max 16
    private String licensedState; // 2

    @SuppressWarnings("UnusedDeclaration")
    protected Driver() {
        // for mongolink
    }

    public Driver(String firstName, String lastName, String emailAddress, String password, String addressLine1, String addressLine2, String city, String state,
                   String zip, String phoneNumber, String drivingLicense, String licensedState) {
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
        this.drivingLicense = drivingLicense;
        this.licensedState = licensedState;

    }

    public boolean isValid() throws Exception{
        //valid firstName length
        if (this.firstName.isEmpty() || this.firstName.length() >50){
            throw new Exception("The firstName length should not greater than 50 Characters");
        }
        //valid lastName length
        if (this.lastName.isEmpty() || this.lastName.length() > 50) {
            throw new Exception("The lastName length should not greater than 50 Characters");
        }
        //valid addressLine1 length
        if (this.addressLine1.isEmpty() || this.addressLine1.length() >100){
            throw new Exception("The firstName length should not greater than 50 Characters");
        }
        //valid addressLine2 length, addressLine2 is optional, could be empty
        if (this.addressLine2.length() > 100) {
            throw new Exception("The lastName length should not greater than 50 Characters");
        }
        //valid passowrd
        if (this.password.isEmpty() || this.password.length()<8 || this.password.length()>20) {
            throw new Exception("The password length should >8 & <20");
        }
        //city
        if (this.city.isEmpty() ||  this.city.length() > 50) {
            throw new Exception("The city length should not greater than 50 Characters");
        }
        //state
        if (this.state.isEmpty() ||  this.state.length() != 2) {
            throw new Exception("The state length must be 2");
        }
        //zip
        if (this.zip.isEmpty() || this.zip.length()!=5) {
            throw new Exception("The zip length must be 2");
        }
        //emailAddress

        {
            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            if (pattern.matcher(this.emailAddress).matches() == false) {
                throw new Exception("Invalid emailAddress");
            }
        }

        //phone#
        {
            Pattern pattern = Pattern.compile("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}");
            if (pattern.matcher(this.phoneNumber).matches() == false) {
                throw new Exception("Invalid phone #");
            }
        }

        //zip
        if (this.drivingLicense.isEmpty() || this.drivingLicense.length()> 16) {
            throw new Exception("The drivingLicense length greater than 16");
        }

        //zip
        if (this.licensedState.isEmpty() || this.licensedState.length()!=2 ) {
            throw new Exception("The zip length must be 2");
        }

        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}