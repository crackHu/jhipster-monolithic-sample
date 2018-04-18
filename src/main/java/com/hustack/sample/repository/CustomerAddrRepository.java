package com.hustack.sample.repository;

import com.hustack.sample.domain.CustomerAddr;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the CustomerAddr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerAddrRepository extends JpaRepository<CustomerAddr, Long> {

}
