package com.groupeisi.ms2.repository;

import com.groupeisi.ms2.domain.Bills;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bills entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillsRepository extends JpaRepository<Bills, Long> {}
