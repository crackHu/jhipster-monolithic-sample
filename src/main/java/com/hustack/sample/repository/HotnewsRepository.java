package com.hustack.sample.repository;

import com.hustack.sample.domain.Hotnews;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Hotnews entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotnewsRepository extends JpaRepository<Hotnews, Long> {

}
