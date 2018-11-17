package com.deepak.assignment.audiologistapi.controller;

import com.deepak.assignment.audiologistapi.domain.*;
import com.deepak.assignment.audiologistapi.service.AudiologistService;
import com.deepak.assignment.audiologistapi.service.CustomerAppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * This class is a controller class which exposes the endpoints required for Audiologist to do below activities
 * 1. Create a new Customer entry
 * 2. Create appointments with the customer
 * 3. Get a list of all appointments and their ratings
 * 4. Get a list of next weeks appointments
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class AudiologistController {

    @Autowired
    AudiologistService audiologistService;

    @Autowired
    CustomerAppointmentService customerAppointmentService;

    @PostMapping("/audiologist/customer")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
        log.debug("createCustomer method start");

        HttpHeaders responseHeaders = new HttpHeaders();

        //ToDO return 201 for created, 400 for any validations exceptions in the request, 500 for any server error
        audiologistService.saveCustomer(customer);

        // Response code 201
        return new ResponseEntity<String>("Successfully created the Customer", responseHeaders, HttpStatus.CREATED);
    }

    @PostMapping("/audiologist/appointment")
    public ResponseEntity<String> createAppointments(@Valid @RequestBody CustomerAppointment customerAppointment) {
        log.debug("createAppointments start");
        //ToDO return 201 for created, 400 for any validations exceptions in the request, 500 for any server error

        //Check if the customer exists in DB, else return 404 from the API
        Optional<Customer> customerOptional = audiologistService.findCustomerById(customerAppointment.getCustomerId());

        //Check if the audiologist exists in DB, else return 404 from the API
        Optional<Audiologist> audiologistOptional = audiologistService.findAudiologistById(customerAppointment.getAudiologistId());
        HttpHeaders responseHeaders = new HttpHeaders();

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<String>("Customer not found in the application", responseHeaders, HttpStatus.NOT_FOUND);
        }

        if (!audiologistOptional.isPresent()) {
            return new ResponseEntity<String>("Audiologist not found in the application", responseHeaders, HttpStatus.NOT_FOUND);
        }

        CustomerAppointmentDTO customerAppointmentDTO = getCustomerAppointmentDTOFromRequest(customerAppointment, customerOptional.get(), audiologistOptional.get());

        //Return 422 for parsing exceptions of the appointment date
        if (customerAppointmentDTO == null) { //Some exception when parsing the amount and date fields
            return new ResponseEntity<String>("Parsing of the appointment date failed", responseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        customerAppointmentService.saveAppointment(customerAppointmentDTO);

        // Response code 201
        return new ResponseEntity<String>("Successfully created the Appointment for customer", responseHeaders, HttpStatus.CREATED);

    }

    /*
     * Gets list of all appointments
     * */

    @GetMapping("/audiologist/appointments/{audiologistId}")
    public List<CustomerAppointmentResponse> getAppointments(@PathVariable int audiologistId, @RequestParam(required = false) String allResults) {
        log.debug("getAppointments start");

        boolean fetchAllRecords = false;

        if (!StringUtils.isEmpty(allResults) && allResults.trim().equalsIgnoreCase("yes")) {
            fetchAllRecords = true;
        }


        return customerAppointmentService.getAllAppointments(audiologistId, fetchAllRecords);
    }


    /*
     * Gets next appointment for the customer  $(customer id)
     * */

    @GetMapping("/customer/{customerId}/appointment")
    public Optional<CustomerAppointmentDTO> getNextAppointmentForCustomer(@PathVariable int customerId) {
        log.debug("getNextAppointmentForCustomer start");

        Optional<CustomerAppointmentDTO> customerAppointmentOptional = customerAppointmentService.getNextAppointmentForCustomer(customerId);
        log.debug("customerAppointmentOptional ={}", customerAppointmentOptional);

        return customerAppointmentOptional;
    }


    /*
     * Post a rating to the latest appointment
     * */
    @PostMapping("/customer/{customerId}/appointment")
    public ResponseEntity<String> rateCustomerAppointment(@PathVariable int customerId, @Valid @RequestBody CustomerAppointment customerAppointment) {
        log.debug("rateCustomerAppointment start");

        HttpHeaders responseHeaders = new HttpHeaders();

        Optional<CustomerAppointmentDTO> customerAppointmentOptional = customerAppointmentService.getLastAppointmentForCustomer(customerId);

        //Check if the customer exists in DB, else return 404 from the API
        Optional<Customer> customerOptional = audiologistService.findCustomerById(customerAppointment.getCustomerId());

        //Check if the audiologist exists in DB, else return 404 from the API
        Optional<Audiologist> audiologistOptional = audiologistService.findAudiologistById(customerAppointment.getAudiologistId());

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<String>("Customer not found in the application", responseHeaders, HttpStatus.NOT_FOUND);
        }

        if (!audiologistOptional.isPresent()) {
            return new ResponseEntity<String>("Audiologist not found in the application", responseHeaders, HttpStatus.NOT_FOUND);
        }
        //Get latest appointment, if not return 204

        CustomerAppointmentDTO customerAppointmentDTO = getCustomerAppointmentDTOFromRequest(customerAppointment, customerOptional.get(), audiologistOptional.get());

        log.debug("customerAppointmentOptional={}", customerAppointmentOptional);

        if (customerAppointmentOptional.isPresent()) {
            log.info("saving rating for appointment...");
            log.debug("customerAppointmentDTO={}", customerAppointmentDTO);
            customerAppointmentOptional.get().setRating(customerAppointmentDTO.getRating());
            customerAppointmentOptional.get().setRatingComments(customerAppointmentDTO.getRatingComments());
            customerAppointmentService.saveAppointment(customerAppointmentOptional.get());
        } else {
            return new ResponseEntity<String>("No Appointments for the customer", responseHeaders, HttpStatus.NO_CONTENT);
        }


        //Return 201
        return new ResponseEntity<String>("Successfully created the rating for appointment", responseHeaders, HttpStatus.CREATED);

    }



    private CustomerAppointmentDTO getCustomerAppointmentDTOFromRequest(CustomerAppointment customerAppointment, Customer customer, Audiologist audiologist) {
        log.debug("creating appointment DTO object from request..");
        //if date is a past date return null

        LocalDateTime appointmentDateTime = null;

        try {
            appointmentDateTime = LocalDateTime.parse(customerAppointment.getAppointmentTimeStamp());
        } catch (Exception exception) {
            //Any parsing exceptions we return null object and return the valid code in the controller
            //We can either use the Controller Advise and do the response re direction with the required status code
            log.error("Parsing Exception , e =" + exception);
            return null;
        }

        CustomerAppointmentDTO customerAppointmentDTO = CustomerAppointmentDTO.builder()
                .appointmentTimeStamp(appointmentDateTime)
                .customer(customer)
                .audiologist(audiologist)
                .rating(customerAppointment.getRating())
                .ratingComments(customerAppointment.getRatingComments())
                .build();

        return customerAppointmentDTO;

    }


}
