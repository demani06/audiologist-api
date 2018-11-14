package com.deepak.assignment.audiologistapi.controller;

import com.deepak.assignment.audiologistapi.domain.Customer;
import com.deepak.assignment.audiologistapi.domain.CustomerAppointment;
import com.deepak.assignment.audiologistapi.service.AudiologistService;
import com.deepak.assignment.audiologistapi.service.CustomerAppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
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

    @PostMapping("/customer")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
        log.debug("createCustomer method start");

        HttpHeaders responseHeaders = new HttpHeaders();

        //ToDO return 201 for created, 400 for any validations exceptions in the request, 500 for any server error
        audiologistService.saveCustomer(customer);

        // Response code 201
        return new ResponseEntity<String>("Successfully created the Customer", responseHeaders, HttpStatus.CREATED);
    }

    @PostMapping("/appointment")
    public ResponseEntity<?> createAppointments(@Valid @RequestBody CustomerAppointment customerAppointment) {
        log.debug("createAppointments start");
        //ToDO return 201 for created, 400 for any validations exceptions in the request, 500 for any server error

        //Check if the customer exists in DB, else return 404 from the API
        Optional<Customer> customerOptional = audiologistService.findCustomerById(customerAppointment.getCustomerId());
        HttpHeaders responseHeaders = new HttpHeaders();

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<String>("Customer not found in the application", responseHeaders, HttpStatus.NOT_FOUND);
        }

        CustomerAppointmentDTO customerAppointmentDTO = getCustomerAppointmentDTOFromRequest(customerAppointment, customerOptional.get());

        //Return 422 for parisng exceptions of the appointment date
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

    @GetMapping("/appointments")
    public List<CustomerAppointmentDTO> getAppointments() {
        log.debug("getAppointments start");
        //ToDO return 200 for created, 400 for any validations exceptions in the request,
        // 404 for audiologist not found
        // 500 for any server error
        return customerAppointmentService.getAllAppointments();
    }


    private CustomerAppointmentDTO getCustomerAppointmentDTOFromRequest(CustomerAppointment customerAppointment, Customer customer) {
        log.debug("creating appointment DTO object from request..");
        //if date is a past date return null

        ZonedDateTime appointmentDateTime = null;

        try {
            appointmentDateTime = ZonedDateTime.parse(customerAppointment.getAppointmentTimeStamp());
        } catch (Exception exception) {
            //Any parsing exceptions we return null object and return the valid code in the controller
            //We can either use the Controller Advise and do the response re direction with the required status code
            log.error("Parsing Exception , e =" + exception);
            return null;
        }

        CustomerAppointmentDTO customerAppointmentDTO = CustomerAppointmentDTO.builder().
                appointmentTimeStamp(appointmentDateTime).
                customer(customer)
                .build();

        return customerAppointmentDTO;

    }


}
