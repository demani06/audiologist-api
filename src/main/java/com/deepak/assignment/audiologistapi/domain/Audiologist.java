package com.deepak.assignment.audiologistapi.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audiologist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "audiologist_id")
    private int audiologistId;
    private String firstName;
    private String lastName;
}
