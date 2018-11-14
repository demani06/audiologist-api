package com.deepak.assignment.audiologistapi.service;

import com.deepak.assignment.audiologistapi.domain.Customer;
import com.deepak.assignment.audiologistapi.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * This class is a service level class which encapsulates the repository/DB logic
 */
@Slf4j
@Service
public class AudiologistService {

    @Autowired
    CustomerRepository customerRepository;

    public void saveCustomer(@Valid Customer customer) {
        log.debug("saving customer .....");
        customerRepository.save(customer);
        log.debug("saved customer...");
    }
}
