package com.deepak.assignment.audiologistapi.repository;

import com.deepak.assignment.audiologistapi.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface repository for Customer related DB operations
 * Spring data does the magic of linking to the actual method at the run time
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {


}
