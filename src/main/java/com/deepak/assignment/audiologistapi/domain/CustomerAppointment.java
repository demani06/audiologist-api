package com.deepak.assignment.audiologistapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_appointment_id")
    private int customerAppointmentId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private int customerId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audiologist_id", nullable = false)
    private int audiologistId;
    @NotNull
    private String appointmentTimeStamp;

}
