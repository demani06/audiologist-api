# audiologist-api

## Few of the Design considerations
- App to be developed on Spring boot and Rest API
- Followed Domain driven design, so first step is to design the Entities.
- This project would be having following entities
    1. Audiologist (to hold the Audiologist data)
    2. Customer (to hold the Customer data)
    3. CustomerAppointment  (to hold the Customer appointments data)
- Did not consider security to differentiate an audiologist and a customer, the assumption is that it is already done or not needed as mentioned in the requirements document
- Exception handling to be done by the global exception handler
- Logging to be done by Sl4j/logback 


## API spec

|API URI	                                |API description	                                         |Method|
|------------------------------------------ |------------------------------------------------------------|-------|
|/api/audiologist/customer	                |as an audiologist I want to create a new customer entry	 | POST|
|/api/audiologist/appointment	|as an audiologist I want to create appointments with a customer	         |POST|
|/api/audiologist/appointments/{audiologistId}?allResults=true|	as an audiologist I want to get a list of all appointments and their ratings	|GET|
|/api/audiologist/appointments/{audiologistId}	|as an audiologist I want to get a list of the next week’s appointments|	GET|
|/api/customer/{customerId}/appointment	|as a customer I want to get the next appointment	|GET|
|/api/customer/{customerId}/appointment	|as a customer I want to rate the last appointment	|POST|



## Procedure to run this application without Docker
- Prerequisites are Java 1.8+, Git Bash Client to download the project using git clone or can be downloaded manually as well
- To build the application, clone the project and run the below command (from the folder where we can see the pom.xml) to build the jar
    **mvn clean install**
- To run the application, please run the below command
    **mvn spring-boot:run**
    
## Procedure to run this application with Docker
- Prerequisites are Docker installed on the machine
- Run the following command to build the image from the base directory (where the  POM.xml and dockerfile resides)
        - on Linux - **sh mvnw install dockerfile:build**
        - on Windows - **mvnw install dockerfile:build**

- Run the following command to build the image from the base directory (where the  POM.xml and dockerfile resides)
       **docker run -d -p 8080:8080 springio/audiologist-api**


## General Documentation about the API 
- This API uses H2 database backed by a file storage for simplicity
- The API uses Swagger documentation. 
- Swagger UI provides an UI to view the endpoints and also one can test the endpoints directly from - this UI
- Only unit test cases are provided and these can be extended to write the integration test cases
- Logging for microservices can be done using Sleuth API which can be integrated with Zipkin, but not used in this application
- Minimum test coverage considering the time frame
- Note date-time format for appointment to follow the format **yyyy-mm-ddThh:mi** (where T is format required for LocalDateTime to parse) .Ex - 2018-11-22T13:59

## Swagger URL for the application after starting the service
Swagger UI - http://localhost:8080/swagger-ui.html#

