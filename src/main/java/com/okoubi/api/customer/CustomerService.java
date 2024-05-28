package com.okoubi.api.customer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<Customer> findAll() {
        return this.customerRepository.findAll().stream()
                .map(customerMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Customer> findById(long customerId) {
        return this.customerRepository.findByIdOptional(customerId)
                .map(customerMapper::toDomain);
    }

    @Transactional
    public Customer create(@Valid Customer customer) {
        CustomerEntity entity = this.customerMapper.toEntity(customer);
        this.customerRepository.persist(entity);
        return this.customerMapper.toDomain(entity);
    }

    @Transactional
    public Customer update(@Valid Customer customer) {
        CustomerEntity entity = this.customerRepository.findById(customer.customerId());
        entity.prenoms = customer.prenoms();
        entity.middleName = customer.middleName();
        entity.nom = customer.nom();
        entity.suffix = customer.suffix();
        entity.email = customer.email();
        entity.phoneMobile = customer.phoneMobile();
        this.customerRepository.persist(entity);
        return this.customerMapper.toDomain(entity);
    }

    @Transactional
    public void delete(long customerId) {
        this.customerRepository.deleteById(customerId);
    }

}