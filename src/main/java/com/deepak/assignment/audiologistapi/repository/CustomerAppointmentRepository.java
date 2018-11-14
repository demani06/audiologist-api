package com.deepak.assignment.audiologistapi.repository;

import com.deepak.assignment.audiologistapi.controller.CustomerAppointmentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface repository for Customer appointment related DB operations
 * Spring data provides a lot of out of box methods, ex - findById
 */
@Repository
public interface CustomerAppointmentRepository extends JpaRepository<CustomerAppointmentDTO, Integer> {

}
