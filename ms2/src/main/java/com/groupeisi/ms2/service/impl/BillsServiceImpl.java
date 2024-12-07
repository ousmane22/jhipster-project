package com.groupeisi.ms2.service.impl;

import com.groupeisi.ms2.domain.Bills;
import com.groupeisi.ms2.repository.BillsRepository;
import com.groupeisi.ms2.service.BillsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.groupeisi.ms2.domain.Bills}.
 */
@Service
@Transactional
public class BillsServiceImpl implements BillsService {

    private static final Logger LOG = LoggerFactory.getLogger(BillsServiceImpl.class);

    private final BillsRepository billsRepository;

    public BillsServiceImpl(BillsRepository billsRepository) {
        this.billsRepository = billsRepository;
    }

    @Override
    public Bills save(Bills bills) {
        LOG.debug("Request to save Bills : {}", bills);
        return billsRepository.save(bills);
    }

    @Override
    public Bills update(Bills bills) {
        LOG.debug("Request to update Bills : {}", bills);
        return billsRepository.save(bills);
    }

    @Override
    public Optional<Bills> partialUpdate(Bills bills) {
        LOG.debug("Request to partially update Bills : {}", bills);

        return billsRepository
            .findById(bills.getId())
            .map(existingBills -> {
                if (bills.getAmount() != null) {
                    existingBills.setAmount(bills.getAmount());
                }
                if (bills.getBillDate() != null) {
                    existingBills.setBillDate(bills.getBillDate());
                }
                if (bills.getCustomerId() != null) {
                    existingBills.setCustomerId(bills.getCustomerId());
                }

                return existingBills;
            })
            .map(billsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bills> findAll() {
        LOG.debug("Request to get all Bills");
        return billsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bills> findOne(Long id) {
        LOG.debug("Request to get Bills : {}", id);
        return billsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Bills : {}", id);
        billsRepository.deleteById(id);
    }
}
