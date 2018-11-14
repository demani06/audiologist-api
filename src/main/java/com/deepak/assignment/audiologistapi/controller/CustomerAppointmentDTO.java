package com.deepak.assignment.audiologistapi.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAppointmentDTO {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerAppointmentId;

    @JsonProperty("customer_id")
    @NotNull
    private int customerId;

    @JsonProperty("appointment_time")
    @NotNull
    private ZonedDateTime appointmentTimeStamp;
}
