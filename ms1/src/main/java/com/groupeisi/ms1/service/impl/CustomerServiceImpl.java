package com.groupeisi.ms1.service.impl;

import com.groupeisi.ms1.domain.Customer;
import com.groupeisi.ms1.repository.CustomerRepository;
import com.groupeisi.ms1.service.CustomerService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.groupeisi.ms1.domain.Customer}.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        LOG.debug("Request to save Customer : {}", customer);
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        LOG.debug("Request to update Customer : {}", customer);
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> partialUpdate(Customer customer) {
        LOG.debug("Request to partially update Customer : {}", customer);

        return customerRepository
            .findById(customer.getId())
            .map(existingCustomer -> {
                if (customer.getName() != null) {
                    existingCustomer.setName(customer.getName());
                }
                if (customer.getEmail() != null) {
                    existingCustomer.setEmail(customer.getEmail());
                }
                if (customer.getPhone() != null) {
                    existingCustomer.setPhone(customer.getPhone());
                }
                if (customer.getAddress() != null) {
                    existingCustomer.setAddress(customer.getAddress());
                }

                return existingCustomer;
            })
            .map(customerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        LOG.debug("Request to get all Customers");
        return customerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findOne(Long id) {
        LOG.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Customer : {}", id);
        customerRepository.deleteById(id);
    }
}
