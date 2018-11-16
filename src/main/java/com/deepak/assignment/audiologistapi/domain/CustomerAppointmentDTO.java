package com.deepak.assignment.audiologistapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity(name = "customer_appointments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CustomerAppointmentDTO {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;
    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne
    @JoinColumn(name = "audiologist_id", nullable = false)
    Audiologist audiologist;
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_appointment_id")
    private int customerAppointmentId;
    @JsonProperty("appointment_time")
    @NotNull
    private LocalDateTime appointmentTimeStamp;

}
