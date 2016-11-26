package com.team4.uberapp.driver;

/**
 * Driver Class defines the object format for car in MongoDB
 *
 * @author  Lin Zhai  & Hector Guo
 * @version 0.3
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team4.uberapp.domain.Validable;
import com.team4.uberapp.util.ErrorReport;

import java.util.UUID;
import java.util.regex.Pattern;

//@Data
//@JsonIgnoreProperties( { "password" })
public class Driver implements Validable, Cloneable {
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
    private String drivingLicense; // max 16
    private String licensedState; // 2

    @SuppressWarnings("UnusedDeclaration")
    protected Driver() {
        // for mongolink
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getLicensedState() {
        return licensedState;
    }

    public void setLicensedState(String licensedState) {
        this.licensedState = licensedState;
    }

    /**
     * Instantiates a new Driver.
     *
     * @param firstName      the first name
     * @param lastName       the last name
     * @param emailAddress   the email address
     * @param password       the password
     * @param addressLine1   the address line 1
     * @param addressLine2   the address line 2
     * @param city           the city
     * @param state          the state
     * @param zip            the zip
     * @param phoneNumber    the phone number
     * @param drivingLicense the driving license
     * @param licensedState  the licensed state
     */
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
        if (this.firstName == null || this.firstName.isEmpty() || this.firstName.length() >50){
            throw new Exception(ErrorReport.toJson(1001, "The firstName length should not greater than 50 Characters"));
        }
        //valid lastName length
        if (this.lastName == null || this.lastName.isEmpty() || this.lastName.length() > 50) {
            throw new Exception(ErrorReport.toJson(1001, "The lastName length should not greater than 50 Characters"));
        }
        //valid addressLine1 length
        if (this.addressLine1 == null || this.addressLine1.isEmpty() || this.addressLine1.length() >100){
            throw new Exception(ErrorReport.toJson(1001, "The firstName length should not greater than 100 Characters"));
        }
        //valid addressLine2 length, addressLine2 is optional, could be empty
        if (this.addressLine2 == null || this.addressLine2.length() > 100) {
            throw new Exception(ErrorReport.toJson(1001, "The lastName length should not greater than 100 Characters"));
        }
        //valid passowrd
        //if (this.password == null || this.password.isEmpty() || this.password.length()<8 || this.password.length()>20) {
        //    throw new Exception(ErrorReport.toJson1001, ("The password length should >8 & <20"));
        //}
        //city
        if (this.city == null || this.city.isEmpty() ||  this.city.length() > 50) {
            throw new Exception(ErrorReport.toJson(1001, "The city length should not greater than 50 Characters"));
        }
        //state
        if (this.state == null || this.state.isEmpty() ||  this.state.length() != 2) {
            throw new Exception(ErrorReport.toJson(1001, "The state length must be 2"));
        }
        //zip
        if (this.zip == null || this.zip.isEmpty() || this.zip.length()!=5) {
            throw new Exception(ErrorReport.toJson(1001, "The zip length must be 5"));
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
                throw new Exception(ErrorReport.toJson(1001, "Invalid phone #"));
            }
        }

        //drivingLicense
        if (this.drivingLicense == null || this.drivingLicense.isEmpty() || this.drivingLicense.length()> 16) {
            throw new Exception(ErrorReport.toJson(1001, "The drivingLicense length greater than 16"));
        }

        //licensedState
        if (this.licensedState == null || this.licensedState.isEmpty() || this.licensedState.length()!=2 ) {
            throw new Exception(ErrorReport.toJson(1001, "The licensedState length must be 2"));
        }

        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}