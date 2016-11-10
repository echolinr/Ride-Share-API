package com.team4.uberapp.passenger;

import com.team4.uberapp.domain.Validable;
import lombok.Data;

import java.util.UUID;

/**
 * Created by lzhai on 2016/11/10.
 */
@Data
public class Passenger implements Validable, Cloneable {
    private UUID id;
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

    @SuppressWarnings("UnusedDeclaration")
    protected Passenger() {
        // for mongolink
    }


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
        if (this.firstName.length() >50){
            throw new Exception("The firstName length should not greater than 50 Characters");
        }
        //valid lastName length
        if (this.lastName.length() > 50) {
            throw new Exception("The lastName length should not greater than 50 Characters");
        }
        //valid addressLine1 length
        if (this.addressLine1.length() >100){
            throw new Exception("The firstName length should not greater than 50 Characters");
        }
        //valid addressLine2 length
        if (this.addressLine2.length() > 100) {
            throw new Exception("The lastName length should not greater than 50 Characters");
        }
        //valid passowrd
        if (this.password.length()<8 || this.password.length()>20) {
            throw new Exception("The password length should >8 & <20");
        }
        //city
        if (this.city.length() > 50) {
            throw new Exception("The city length should not greater than 50 Characters");
        }
        //state
        if (this.state.length() != 2) {
            throw new Exception("The state length must be 2");
        }
        //zip
        if (this.zip.length()!=5) {
            throw new Exception("The zip length must be 2");
        }
        //emailAddress

        {
        //    final Pattern pattern = Pattern.compile("/[a-zA-Z0-9_.\\-]+\\@[a-zA-Z](([a-zA-Z0-9-]+).)*/");
          //  if (!pattern.matcher(this.emailAddress).matches()) {
            //    throw new Exception("Invalid emailAddress");
           // }
        }

        //phone#
        {
            //final Pattern pattern = Pattern.compile("/^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/");
           // if (!pattern.matcher(this.phoneNumber).matches()) {
           //     throw new Exception("Invalid phone #");
           // }
        }

        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
