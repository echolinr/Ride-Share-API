# Ride-Share-API - Independent Study

##Schedule

| Week | Activity | 
|------|----|
|1	| Prepare Environment |
|2	| Implement Milestone 1 API |
|3	| Implement Milestone 2 API	|
|4	| Implement Encryption, Authentication & Access Control and Unit Tests (Milestone 3)|
|5 	| Complete Unit Tests for Full Coverage (Milestone 4)|
|6 | Test Scaling (Milestone 5)|
|7 	| Test Performance (Mileston 6)|
| 8 | Test Reliability (Mielstone 7)|


## Technology

+ Spark
+ Jackson
+ MongoLink

# API Overview

Recommendations in the following references were used as a general guideline while definig these API specs.

+ [Best Practices for a Pragmatic Restul API](http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api)
+ [Apigee API Design Book](https://pages.apigee.com/rs/apigee/images/api-design-ebook-2012-03.pdf)
+ [HTTP Status Codes](http://www.restapitutorial.com/httpstatuscodes.html)

## URL Naming Convention

API URL requests have the following components which are supplied by the client

+ **Method**, which determines the type of action being requested on a resource
+ **Version**, which indicates the version of the API being requested
+ **Resource URI**, which identifies the resource that performs the action
+ *Optional* **Query String**, which specifies additional control parameters for the resource's actions
+ *Optional* **Request Body**, which specifies data that acts as input to the resource for the action requested

For example, `GET http://api.example.com/v1/drivers?count=30`

In response, the API returns the following

+ **HTTP Status Code**, indicating the successful or failed outcome of the request
+ **Response Body**, indicating the response of the request made

In addition, there may be **Headers** in both the request and the response. 

## Versioning

Currently supported versions of the API and their release date are provided below. All API calls should be prefixed with
the version number `GET /VERSION/cars` even though the version number is not included in the documentation of each
API call.

+ Current Version: `v1` (should be used for `VERSION` in the example call above)

For example, `GET http://api.example.com/v1/drivers?count=30`

## Resources

All resources are named plural (`cars` instead of `car`) to avoid confusion. No singular references anywhere.
While the API might currently accept some singular versions, that capability will be deprecated. We currently support
the following resources.

Resources that have a relationship to each other can be used as a sub-resource within a URI. For example `/drivers/:driverId/cars`
identifies the cars for a particular driver while `/cars` identifies the global list of cars.

### Drivers

`drivers` refers to drivers in the system.

### Passengers

`passengers` refers to passengers in the system.

### Cars

`cars` refers to cars in the system. Cars belong to drivers and cannot exist without a driver. 

### Rides


## Query String Parameters

| Parameter | Current Applicable Resources | Description |
|-----------|----------------------|-------------|
| `count` | all | When returning a list of items, this specifies the maximum elements to return. For example `GET /cars?count=100` returns the first hundred cars even if there are more cars in the system |
| `offsetId` | all | The ID of the last resource already retrieved from a matching list. When provided, server returns resources after this matching element. This is useful in combination with `count` to get the *next page* of a list of elements |
| `sort` | all | Specify the field by which results should be ordered |
| `sortOrder` | all | Used in combination with `sort`, it specifies the order in which to return the elements.  `asc` is for asending or `desc` for descending. Default value is `asc` except for a time-based sort field in which case the default values is `desc` |

Note: In this implementation, only support one field for `sort` and `sortOrder`

## Methods


###  `GET`

+ `GET` is used to retrieve either a list of resources or a single resource at a specific URI.
+ In the case of a list, the data is returned as a JSON array.
+ In case of a single resource, it is returned as a JSON object.

### `POST`

+ `POST` is used to create a new resource on the system.
+ Usually a complete created resource is returned as a JSON object.
+ There are rare circumstances where `POST` is used to create multiple resources (such as adding multiple cars to a driver. We will not do it in this project).
 In this case, a JSON array of created objects or relationships is returned
+ Note that `POST` is the only method that returns `201 Created` on success.

### `PUT`

+ `PUT` is used to replace an entire resource at a URI.
+ A complete copy of the new resource is returned.
+ _We wil rarely use `PUT` since we mostly update specific properties (see `PATCH` below)_
+ `PUT` currently **cannot** be applied in bulk to a list of resources

### `PATCH`

+ `PATCH` is used to update parts of a resource at a URI.
+ A complete copy of the updated resource is returned.
+ `PATCH` currently **cannot** be applied in bulk to a list of resources
+ _Our `PATCH` implementation is not compliant with standards but is consistent with generally-used convention.
 Instead of Request Body being a set of operations, it is a list of fields with updated values_

###  `DELETE`

+ `DELETE` is used to remove a resource from the system
+ `DELETE` **cannot** be applied in bulk to a list of resources

## Response Headers

##  `Status`

For all successful calls, the return status is `2XX`

+ For `POST`, the success status code is `201 Created`
+ For all other methods, the success status code is `200 OK`

In case of errors, specify the appropriate return status code. 

+ 400 Bad Request: If there's any problem with the information provided by the client and it cannot be processed properly. 
+ 401 Unathorized: The credential information provided could not be verified or is invalid
+ 404 Not Found: Request is valid, but the resource authorized is not available
+ 500 Internal Server Error: Request is OK, but there was unexpected problem on the server



## `Content-Type`

+ All calls return a Content-Type of `application/json`

## Error Handling

In addition to providing one of the error Status Codes mentioned above, the Response Body will contain a JSON object with details of the error.

	{
		"statusCode": number // Sames as HTTP status code
		"errorCode": number // A more detailed error code
		"errorMessage": string		// human readable error message
	}


# API Specification



## Drivers

Following are two types of responses that are returned by the `drivers` resources.

#### Single Driver

	{
	   "id": GUID,
	   "firstName" : String(50),
	   "lastName" : String(50),
	   "emailAddress" : String(50), Valid Format,
	   "password" : String(20, Min 8)
	   "addressLine1" : String(100),
	   "addressLine2" : String(100) - Optional,
	   "city" : String(50),
	   "state" : String (2, Min 2),
	   "zip" : String(5, Min 5)
	   "phoneNumber" : String - XXX-XXX-XXXX
	   "drivingLicense" : String(16)
	   "licensedState" : String(2)
	}

#### Multiple Drivers

	[
		{
			"id": GUID,
			"firstName" : String,
			"lastName" : String
			...
			...
		}, ...
	]

###  GET /drivers

Returns the list of all drivers in the system. 

### POST /drivers

Creates a new driver in the system with the object that is passed to it. All fields in the body below are required. This call returns
an modified JSON containing the internally generated `id` and other properties populated by the system.

On failure, this will generate a `400 Bad Request` with a description of the failure.

#### Request Body

     {
       "firstName" : String,
       "lastName" : String
       ...
       ...
      }


### GET /drivers/:driverId

Returns a driver matching the provided ID. For example, `GET /VERSION/drivers/770877af-7d08-447a-8add-ccfd507ce170`
will return a JSON representing the driver with that id. 

      {
       "id": GUID,
       "firstName" : String,
       "lastName" : String
       ...
       ...
    }


### PATCH /drivers/:driverId

This will allow partial updates of specific fields in the driver. For example, calling
`PATCH /VERSION/drivers/0db87174-4e11-4369-a8fd-8339ed3abd37`
with the first Request Body below
will update `firstName` property of the driver in the JSON Body. The `id` field in the body is optional,
though if present, it will have to match the one in the URL.

This will return a `200 OK` if the update was successful. If the `id` or any field in the body conflicts with the request,
a `404 NotFound` will be thrown with the reason.

The response will contain the updated driver.


#### Request Body
      {
        "firstName": "John"
      }

or

      {
        "firstName": "John",
        "lastName": "Doe"
        ...
        ...
      }

### DELETE /drivers/:driverId

This deletes a driver with the given Id from the system

For example, `DELETE /VERSION/drivers/0db87174-4e11-4369-a8fd-8339ed3abd37`
will delete the driver and return the json back for confirmation with a `200 OK`




## Passengers

Following are two types of responses that are returned by the `Passengers` resources.

#### Single Passenger

	{
	   "id": GUID,
	   "firstName" : String(50),
	   "lastName" : String(50),
	   "emailAddress" : String(50), Valid Format,
	   "password" : String(20, Min 8)
	   "addressLine1" : String(100),
	   "addressLine2" : String(100) - Optional,
	   "city" : String(50),
	   "state" : String (2, Min 2),
	   "zip" : String(5, Min 5)
	   "phoneNumber" : String - XXX-XXX-XXXX
	}

#### Multiple Passengers

	[
		{
			"id": GUID,
			"firstName" : String,
			"lastName" : String
			...
			...
		}, ...
	]

###  GET /passengers

Returns the list of all Passengers in the system. 

### POST /passengers

Creates a new passenger in the system with the object that is passed to it. All fields in the body below are required. This call returns
an modified JSON containing the internally generated `id` and other properties populated by the system.

On failure, this will generate a `400 Bad Request` with a description of the failure.

#### Request Body

     {
       "firstName" : String,
       "lastName" : String
       ...
       ...
      }


### GET /passengers/:passengerId

Returns a passenger matching the provided ID. For example, `GET /VERSION/passengers/770877af-7d08-447a-8add-ccfd507ce170`
will return a JSON representing the passenger with that id. 

      {
       "id": GUID,
       "firstName" : String,
       "lastName" : String
       ...
       ...
    }

### PATCH /passengers/:passengerId

This will allow partial updates of specific fields in the passenger. For example, calling
`PATCH /VERSION/passengers/0db87174-4e11-4369-a8fd-8339ed3abd37`
with the first Request Body below
will update `firstName` property of the passenger in the JSON Body. The `id` field in the body is optional,
though if present, it will have to match the one in the URL.

This will return a `200 OK` if the update was successful. If the `id` or any field in the body conflicts with the request,
a `404 NotFound` will be thrown with the reason.

The response will contain the updated passenger.


#### Request Body
      {
        "firstName": "John"
      }

or

      {
        "firstName": "John",
        "lastName": "Doe"
        ...
        ...
      }

### DELETE /passengers/:passengerId

This deletes a passenger with the given Id from the system

For example, `DELETE /VERSION/passengers/0db87174-4e11-4369-a8fd-8339ed3abd37`
will delete the passenger and return the json back for confirmation with a `200 OK`


## Cars

Following are two types of responses that are returned by the `cars` resources. ***For this week's implementation, we will ignore that Cars belong to Drivers and simply implement them as a top-level resource.***

#### Single Car

	{
	   "id": GUID,
	   "make": String(50),
	   "model": String(50),
	   "license" : String(10),
	   "carType" : String(10),
	   "maxPassengers" : number,
	   "color" : String(20),
	   "validRideTypes" : String(16) Array // Values are ECONOMY, PREMIUM, EXECUTIVE
	}

#### Multiple Cars

	[
		{
			"id": GUID,
		   "make": String(50),
		   "model": String(50),
			...
			...
		}, ...
	]

###  GET /cars

Returns the list of all Cars in the system. 

### POST /cars

Creates a new car in the system with the object that is passed to it. All fields in the body below are required. This call returns
an modified JSON containing the internally generated `id` and other properties populated by the system.

On failure, this will generate a `400 Bad Request` with a description of the failure.

#### Request Body

     {
	   "make": String(50),
	   "model": String(50),
       ...
       ...
      }


### GET /cars/:carId

Returns a car matching the provided ID. For example, `GET /VERSION/cars/770877af-7d08-447a-8add-ccfd507ce170`
will return a JSON representing the car with that id. 

      {
       "id": GUID,
	   "make": String(50),
	   "model": String(50),
       ...
       ...
    }


### PATCH /cars/:carId

This will allow partial updates of specific fields in the car. For example, calling
`PATCH /VERSION/cars/0db87174-4e11-4369-a8fd-8339ed3abd37`
with the first Request Body below
will update `make` property of the car in the JSON Body. The `id` field in the body is optional,
though if present, it will have to match the one in the URL.

This will return a `200 OK` if the update was successful. If the `id` or any field in the body conflicts with the request,
a `404 NotFound` will be thrown with the reason.

The response will contain the updated car.


#### Request Body
      {
	   "make": "Ford"
      }

or

      {
	   "make": "Ford",
	   "model": "Explorer",
        ...
        ...
      }

### DELETE /cars/:carId

This deletes a car with the given Id from the system

For example, `DELETE /VERSION/cars/0db87174-4e11-4369-a8fd-8339ed3abd37`
will delete the car and return the json back for confirmation with a `200 OK`



## Rides

Following are two types of responses that are returned by the `rides` resources. **For this week's implementation, we will ignore that rides have Driver, Car, Passenger and Route Points, and simply implement them as a top-level resource.**

#### Single Ride

	{
	   "id": GUID,
	   "rideType": String(16) // Values are ECONOMY, PREMIUM, EXECUTIVE
	   "startPoint" : { lat: Decimal, long: Decimal }
	   "endPoint": { lat: Decimal, long: Decimal }
		"requestTime" : Number (Timestamp)
		"pickupTime" : Number  (TimeStamp)
		"dropOffTime" : Number, (TimeStamp)
		"status" : String [REQUESTED, AWAITING_DRIVER, DRIVE_ASSIGNED, IN_PROGRESS, ARRIVED, CLOSED]
		"fare": Decimal,
	}

#### Multiple Rides

	[
		{
			"id": GUID,
		   "rideType": String(16) // Values are ECONOMY, PREMIUM, EXECUTIVE
		   "startPoint" : { lat: Decimal, long: Decimal }
			...
			...
		}, ...
	]

###  GET /rides

Returns the list of all Rides in the system. 

### POST /rides

Creates a new ride in the system with the object that is passed to it. All fields in the body below are required. This call returns
an modified JSON containing the internally generated `id` and other properties populated by the system.

On failure, this will generate a `400 Bad Request` with a description of the failure.

#### Request Body

     {
	   "rideType": "ECONOMY";
	   "startPoint" : { lat: 34.5, long: 56.7 }
       ...
       ...
      }


### GET /rides/:rideId

Returns a ride matching the provided ID. For example, `GET /VERSION/rides/770877af-7d08-447a-8add-ccfd507ce170`
will return a JSON representing the ride with that id. 

      {
       "id": GUID,
	   "rideType": "ECONOMY";
	   "startPoint" : { lat: 34.5, long: 56.7 }
       ...
       ...
    }



### PATCH /rides/:rideId

This will allow partial updates of specific fields in the car. For example, calling
`PATCH /VERSION/rides/0db87174-4e11-4369-a8fd-8339ed3abd37`
with the first Request Body below
will update `rideType` property of the ride in the JSON Body. The `id` field in the body is optional,
though if present, it will have to match the one in the URL.

This will return a `200 OK` if the update was successful. If the `id` or any field in the body conflicts with the request,
a `404 NotFound` will be thrown with the reason.

The response will contain the updated ride.


#### Request Body
      {
	   "rideType": "ECONOMY";
      }

or

      {
	   "rideType": "ECONOMY";
	   "startPoint" : { lat: 34.5, long: 56.7 }
        ...
        ...
      }

### DELETE /rides/:rideId

This deletes a ride with the given Id from the system

For example, `DELETE /VERSION/rides/0db87174-4e11-4369-a8fd-8339ed3abd37`
will delete the ride and return the json back for confirmation with a `200 OK`



# API Enhancements 


## Drivers

The following new API end points will allow cars to be created and managed in the context of a driver.
Other actions like `GET` on a single car, `PUT` and `DELETE` will use the `/cars` end point. 

###  GET /drivers/:driverId/cars

Returns the list of all Cars associated with the driver in the system. 

### POST /drivers/:driverId/cars

Creates a new car in the system associated with the specified driver whose ID is given. 
The car is created from the object that is passed to it. All fields in the body below 
are required. This call returns
an modified JSON containing the internally generated `id` and other properties populated by the system.

On failure, this will generate a `400 Bad Request` with a description of the failure.

#### Request Body

     {
	   "make": String(50),
	   "model": String(50),
       ...
       ...
      }
      

### GET /drivers/:driverId/rides

Returns a list of rides associated with the passenger. 
      
## Passengers

### GET /passegers/:passengerId/rides

Returns a list of rides associated with the passenger. 

## Cars

You will delete the following methods (Note: You can repurpose this code for other parts of this milestone)

+ `POST /cars` since you can no longer create cars that are not associated witha driver

###  GET /cars/:carId/drivers

Returns the a single element array containing the driver associated with the car
 
## Rides


### Single Ride Enhancement

Enhance the ride request object to allow for creation with additional references. 


	{
        "id": GUID,
        "rideType": String(16) // Values are ECONOMY, PREMIUM, EXECUTIVE
        "startPoint" : { lat: Decimal, long: Decimal }
        "endPoint": { lat: Decimal, long: Decimal }
        "requestTime" : Number (Timestamp)
        "pickupTime" : Number  (TimeStamp)
        "dropOffTime" : Number, (TimeStamp)
        "status" : String [REQUESTED, AWAITING_DRIVER, DRIVE_ASSIGNED, IN_PROGRESS, ARRIVED, CLOSED]
        "fare": Decimal,
        "driverId" : GUID of the driver,
        "carId" : GUID of the car,
        "passengerId" : GUID of the passenger
	}



### POST /rides/:rideId/routePoints

Creates a  route point on the route taken for the ride. This allows a client to set a series of points on
the route that the driver takes for a given ride. 

    {
        timestamp: Number (TImestamp)
        lat: Decimal,
        long: Decimal
    }

### GET /rides/:rideId/routePoints

Gets the complete list of route points ordered by timestamp (ascending order)

### GET /rides/:rideId/routepoints/latest

Gets the latest (by timestamp) single route point




# Security Implementation

In this milestone, you will implement encryption of key data in the system, 
add authentication and provide access control by implementing two roles
for users. 

## Encryption/Hashing Data

Study and compare __encryption__ and __hashing__ by reading about them on the web.
Below are some useful links for this.

+ [Wikipedia - Encryption](https://en.wikipedia.org/wiki/Encryption)
+ [Secured Password Hashing](http://security.blogoverflow.com/2013/09/about-secure-password-hashing/)
+ [Encryption vs Hashing for Passwords](http://www.darkreading.com/safely-storing-user-passwords-hashing-vs-encrypting/a/d-id/1269374)
+ [Salted Password Hashing](https://crackstation.net/hashing-security.htm)

Once done, do the following to complete this port of the Milestone.

+ Identify a mechanism for storing password that you think works for you. You can also leverage the technique
discussed in the APP class. Create a brief description in your submission document.
+ Implement this mechanism for storing the password. Your `POST` method must accept a string
password, apply  the encryption/hashing mechanism and store the resulting
value. 
+ Change your `GET` methods for `passengers` and `drivers` so that the password
field is not available in the returned response body.


## Authentication

Modify your code to do the following

+ Implement `sessions` API described below. Both drivers and passengers authenticate
with this API. You will know who they are by first testing them against 
drivers and then against passengers.


## Access Control

Implement the following

+ Modify `POST` methods for `drivers` and `passengers` so that you cannot have
a driver and a passenger with the same email address. 
+ Require authentication before `POST` method is called `rides` and `cars`, including `POST` for `cars` within the 
`drivers` resource. Only drivers can call `POST` method on `cars` and, either passengers or drivers can call
`POST` methods on `rides`. For now, you don't have to restrict that a driver can only
create cars for themselves.


## Sessions API

### POST /sessions

Authenticate a user with the following request body

    {
        "email": "john@doe.com",
        "password" : "somepassword"
    }
    
Authenticates the user against the driver and passenger data and returns the following response body

    {
        "token" : "787897dasd78d7f9s7df98sdf89sd7f89sd7f89sdf"
    }
    
    
The token should be hashed and encrypted, and contain  the user's ID and an expiration date.




# Unit Test Implementation

In this milestone, you will implement unit tests for all APIs that you created in the
previous milestones. Coverage of ALL APIs is required for full credit. 

## References
[Spark Unit Testing](https://sparktutorials.github.io/2015/07/30/spark-testing-unit.html)


# Scale Testing your API
 
In this milestone, you will do load testing on the APIs that you created in the
previous milestones. 

+ Create a a GCE instance of the smallest size possible (You can start with `n1-standard-1`)
+ Run load tests on your API. In order to implement this, you will need to create a load test with a common 
 use case (see below)
+ Initiate this use case at the rate of 100 instances every 1 minute, and then repeat by halving the interval (30 seonds, 15 seconds etc.)
+ Record the response times and plot them.
+ Determine the load at which the response time starts to increase non-lineraly


## Use Case for Load Testing
 
+ Create a driver
+ Create a car for the driver
+ Create a passenger
+ Create a ride for the passenger
+ Update the ride with the created Driver and a Car
+ Add route points every 10 seconds for a 2 minute ride
+ Mark the ride as complete
+ Repeat by creating 10 rides for the same passenger, car and driver combination.


## References


