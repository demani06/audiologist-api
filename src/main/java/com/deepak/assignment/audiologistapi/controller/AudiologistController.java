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

        CustomerAppointmentDTO customerAppointmentDTO = getCustomerAppointmentDTOFromRequest(customerAppointment);

        HttpHeaders responseHeaders = new HttpHeaders();

        if (customerAppointmentDTO == null) { //Some exception when parsing the amount and date fields
            return new ResponseEntity<String>("Parsing failed or transaction date is a future date", responseHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
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


    private CustomerAppointmentDTO getCustomerAppointmentDTOFromRequest(@Valid CustomerAppointment customerAppointment) {
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
                customerId(customerAppointment.getCustomerId())
                .build();

        return customerAppointmentDTO;

    }


}
