package com.deepak.assignment.audiologistapi.service;

import com.deepak.assignment.audiologistapi.controller.CustomerAppointmentDTO;
import com.deepak.assignment.audiologistapi.repository.CustomerAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAppointmentService {

    @Autowired
    CustomerAppointmentRepository customerAppointmentRepository;


    public void saveAppointment(CustomerAppointmentDTO customerAppointmentDTO) {
        customerAppointmentRepository.save(customerAppointmentDTO);
    }

    public List<CustomerAppointmentDTO> getAllAppointments() {
        return customerAppointmentRepository.findAll();
    }
}
