package com.deepak.assignment.audiologistapi.repository;

import com.deepak.assignment.audiologistapi.domain.CustomerAppointmentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface repository for Customer appointment related DB operations
 * Spring data provides a lot of out of box methods, ex - findById
 */
@Repository
public interface CustomerAppointmentRepository extends JpaRepository<CustomerAppointmentDTO, Integer> {

    List<CustomerAppointmentDTO> findByAudiologist_audiologistId(Integer audiologistId);

    @Query("select c from com.deepak.assignment.audiologistapi.domain.CustomerAppointmentDTO c" +
            " where c.appointmentTimeStamp <= :endDateTime " +
            "and c.appointmentTimeStamp > :startDateTime ")
    List<CustomerAppointmentDTO> findAllAppointmentsBetween(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    /*        ,
            @Param("audiologistId") Integer audiologistId*/
    );





}
