package com.groupeisi.ms2.service;

import com.groupeisi.ms2.domain.Bills;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.groupeisi.ms2.domain.Bills}.
 */
public interface BillsService {
    /**
     * Save a bills.
     *
     * @param bills the entity to save.
     * @return the persisted entity.
     */
    Bills save(Bills bills);

    /**
     * Updates a bills.
     *
     * @param bills the entity to update.
     * @return the persisted entity.
     */
    Bills update(Bills bills);

    /**
     * Partially updates a bills.
     *
     * @param bills the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Bills> partialUpdate(Bills bills);

    /**
     * Get all the bills.
     *
     * @return the list of entities.
     */
    List<Bills> findAll();

    /**
     * Get the "id" bills.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Bills> findOne(Long id);

    /**
     * Delete the "id" bills.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
