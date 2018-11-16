package com.deepak.assignment.audiologistapi.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAppointmentResponse {

    private String customerName;
    private String audiologistName;
    private String appointmentTime;
}
