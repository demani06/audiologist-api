package com.deepak.assignment.audiologistapi.service;

import com.deepak.assignment.audiologistapi.domain.CustomerAppointmentDTO;
import com.deepak.assignment.audiologistapi.domain.CustomerAppointmentResponse;
import com.deepak.assignment.audiologistapi.repository.CustomerAppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
 * Appointments service wrapper for Customer which interacts with Cutomer Appointment DB Repository
 * */

@Slf4j
@Service
public class CustomerAppointmentService {

    @Autowired
    CustomerAppointmentRepository customerAppointmentRepository;


    public void saveAppointment(CustomerAppointmentDTO customerAppointmentDTO) {
        customerAppointmentRepository.save(customerAppointmentDTO);
    }

    public List<CustomerAppointmentResponse> getAllAppointments(int audiologistId, boolean fetchAllRecords) {

        log.info("Getting all appointments for audiologistId ={}", audiologistId);
        log.info("Getting all appointments , fetchAllRecords ={}", fetchAllRecords);
        List<CustomerAppointmentDTO> customerAppointmentDTOList = null;
        if (fetchAllRecords) {
            customerAppointmentDTOList = customerAppointmentRepository.findByAudiologist_audiologistId(audiologistId);
        } else {
            //customerAppointmentDTOList =  customerAppointmentRepository.findAllAppointmentsBetween(LocalDateTime.now(), LocalDateTime.now().plusDays(7), audiologistId);
            customerAppointmentDTOList = customerAppointmentRepository.findAllAppointmentsBetween(LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        }

        log.info("customerAppointmentDTOList={}", customerAppointmentDTOList);

        List<CustomerAppointmentResponse> appointmentResponseList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

        if (customerAppointmentDTOList.size() > 0) {
            for (CustomerAppointmentDTO customerAppointmentDTO : customerAppointmentDTOList) {
                CustomerAppointmentResponse customerAppointmentResponse = new CustomerAppointmentResponse();
                customerAppointmentResponse.setCustomerName(customerAppointmentDTO.getCustomer().getFirstName() + " " + customerAppointmentDTO.getCustomer().getLastName());
                customerAppointmentResponse.setAudiologistName(customerAppointmentDTO.getAudiologist().getFirstName() + " " + customerAppointmentDTO.getAudiologist().getLastName());
                customerAppointmentResponse.setAppointmentTime(customerAppointmentDTO.getAppointmentTimeStamp().format(formatter));
                appointmentResponseList.add(customerAppointmentResponse);
            }
        }


        return appointmentResponseList;


    }
}
