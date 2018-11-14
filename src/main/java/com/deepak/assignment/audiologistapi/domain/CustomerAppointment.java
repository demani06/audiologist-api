package com.deepak.assignment.audiologistapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAppointment {

    @NotNull
    private int customerId;
    @NotNull
    private String appointmentTimeStamp;

}
