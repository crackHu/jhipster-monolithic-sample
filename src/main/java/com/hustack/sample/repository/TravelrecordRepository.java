package com.hustack.sample.repository;

import com.hustack.sample.domain.Travelrecord;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Travelrecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TravelrecordRepository extends JpaRepository<Travelrecord, Long> {

}
