# audiologist-api

## TODO
1. H2 database run externally - done
2. Create Audiologist entity and save in customer appointments table - pending
3. Apply Security - Pending
4. Exception hanlding and test cases - pending
5. Dockerise - Pending

## Design considerations
- Followed Domain driven design, so first step is design the Entities.
- This project would be having following entities
    1. Audiologist (to hold the Audiologist data)
    2. Customer (to hold the Customer data)
    3. CustomerAppointment  (to hold the Customer appointments data)
- 
- Did not consider security
- 

## API spec

|API URI	                                |API description	                                         |Method|
|------------------------------------------ |------------------------------------------------------------|-------|
|/api/audiologist/customer	                |as an audiologist I want to create a new customer entry	 | POST|
|/api/audiologist/appointments	|as an audiologist I want to create appointments with a customer	         |POST|
|/api/audiologist/appointments/{audiologistId}?allResults=true|	as an audiologist I want to get a list of all appointments and their ratings	|GET|
|/api/audiologist/appointments/{audiologistId}	|as an audiologist I want to get a list of the next week’s appointments|	GET|
|/api/customer/appointment	|as a customer I want to get the next appointment	|GET|
|/api/customer/appointment/rating	|as a customer I want to rate the last appointment	|POST|



## Procedure to run this application
- Prerequisites are Java 1.8+, Git Bash Client
- To build the application, clone the project and run the below command (from the folder where we can see the pom.xml) to build the jar
    mvn clean install
- To run the application, please run the below command
    mvn spring:boot-run


## General Documentation about the API 

- This API uses H2 database backed by a file storage for simplicity
- The API uses Swagger documentation. 
- Swagger UI provides an UI to view the endpoints and also one can test the endpoints directly from - this UI
- Only unit test cases are provided and these can be extended to write the integration test cases
- Logging for microservices can be done using Sleuth API which can be integrated with Zipkin, but not used in this application

## Swagger URL for the application after starting the service
Swagger UI - http://localhost:8080/swagger-ui.html#

