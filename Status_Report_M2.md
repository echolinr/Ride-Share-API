# Status Report Milestone 2
** Naming Conventions (updated on Dec 11,2016)**

Name	Type | Convention 
-----------|----------
class name	| start with uppercase letter and be a noun. e.g. Car.
interface name	| start with uppercase letter. e.g. Vehicle.
method name	|  start with lowercase letter and be a verb e.g. main(), println() etc.
variable name	| should start with lowercase letter e.g. firstName etc.
package name	| should be in lowercase letter e.g. java, lang etc.
constants name	| should be in uppercase letter. e.g. MAX_PRIORITY etc.

**Error Handling (created on Dec 11, 2016)**

Response Body

```json
{
  "errorCode": 2001,
  "errorMsg": "make at most 50 Characters",
  "timestamp": 1475176150652
}
```

Identify all possible error codes for each of the resources and consolidate them into a single table as below. Some 
examples of possible errors are below.

1. Invalid resource name
2. Identifier not matching any resource instance
3. Invalid property name (given in POST)
4. Invalid value for a property (given in POST)


Error Code  | Error Message    | Relevant Resources  | Parameters
----------- | ----------|------------ |-------------
1001  | Invalid parameters or resource {0} given  | Driver  | `0 - Resource Name`
2001  | Invalid parameters or resource {0} given  | Car  | `0 - Resource Name`
3001  | Invalid parameters or resource {0} given  | Passenger  | `0 - Resource Name`
4001  | Invalid parameters or resource {0} given  | Ride  | `0 - Resource Name`
5001  | Invalid parameters or resource {0} given  | Session  | `0 - Resource Name`
----


**Individual Contribution **
Hector 50%
Lin 50%

** Status Report**

Until this report, we've finished following api:

1. all routes for cars and passengers
2. setup routes api for drivers and rides but need to implement all properies
3. GET /drivers/:driverId/cars & POST /drivers/:driverId/cars

** Challenges**
We actually faced a lot of challengs in this project, most of them have been sovled. They are:

1. A little bit bumpy when using IntelliJ at the beginnning. IntelliJ's own java SDK cause a lot of confusion. Spent some time to figure it out. Import a project into IntelliJ has some trick too. build.gradle sometimes not automatic import library
2. understanding Mongolink， we actually started from its simple example to understand persistent and mapping
3. Json processing for request and result returning
4. How to use MongoDB in Java. To better understand it, we actually spent quite some time in learning mongodb, mongodb java driver and even morphia a little.

** Things need help **

 How to use critera, we have not finished this part  in Milestone1: 

*Query String Parameters

Parameter	| Current Applicable Resources |	Description
----------|------------------------------|-------------
'count'	| all	| When returning a list of items, this specifies the maximum elements to return. For example GET /cars?count=100 returns the first hundred cars even if there are more cars in the system
'offsetId' |	all	| The ID of the last resource already retrieved from a matching list. When provided, server returns resources after this matching element. This is useful in combination with count to get the next page of a list of elements
'sort' | 	all	| Specify the field by which results should be ordered
'sortOrder' |	all	| Used in combination with 'sort', it specifies the order in which to return the elements. 'asc' is for asending or 'desc' for descending. Default value is asc except for a time-based sort field in which case the default values is desc
**Note**: In this implementation, you only need to support one field for 'sort' and 'sortOrder'.
