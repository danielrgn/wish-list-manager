package com.challenge.luizalabs.service;

import com.challenge.luizalabs.dto.CustomerDtoRequest;
import com.challenge.luizalabs.dto.CustomerDtoResponse;
import com.challenge.luizalabs.exception.EntityAlreadyExistsException;
import com.challenge.luizalabs.exception.EntityNotFoundException;
import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.repository.CustomerRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService implements ServiceBase<CustomerDtoRequest, CustomerDtoResponse> {

  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public CustomerDtoResponse saveOrUpdate(final CustomerDtoRequest customerDtoRequest,
                                          final Long id) {
    final String email = Optional.ofNullable(customerDtoRequest)
        .map(CustomerDtoRequest::getEmail)
        .orElse("");

    final String name = Optional.ofNullable(customerDtoRequest)
        .map(CustomerDtoRequest::getName)
        .orElse("");

    this.validateCustomerExistsByEmail(email, id);

    final Customer customerObj = Customer.builder()
        .name(name)
        .email(email)
        .build();

    if (id != null) {
      customerObj.setId(id);
    }

    final Customer customerSaved = customerRepository.save(customerObj);

    return CustomerDtoResponse.builder()
          .id(customerSaved.getId())
          .name(customerSaved.getName())
          .email(customerSaved.getEmail())
        .build();
  }

  @Override
  public List<CustomerDtoResponse> getAll() {
    final List<CustomerDtoResponse> dto = new ArrayList<>();
    final List<Customer> customers = this.customerRepository.findAll();

    if (customers.isEmpty()) {
      throw new EntityNotFoundException("Customer");
    }

    for (Customer c : customers) {
      dto.add(CustomerDtoResponse.builder()
          .id(c.getId())
          .name(c.getName())
          .email(c.getEmail())
          .build());
    }

    return dto;
  }

  @Override
  public CustomerDtoResponse getById(final Long id) {
    final Optional<Customer> customer = this.customerRepository.findById(id);
    if (customer.isPresent()) {
      return CustomerDtoResponse.builder()
          .id(customer.get().getId())
          .name(customer.get().getName())
          .email(customer.get().getEmail())
          .build();
    }
    throw new EntityNotFoundException("Customer");
  }

  @Override
  public void delete(final Long id) {
    this.customerRepository.deleteById(id);
  }

  private void validateCustomerExistsByEmail(final String email, final Long id) {
    final Customer customer = customerRepository.findByEmail(email);
    final boolean existCustomer = Optional.ofNullable(customer).isPresent();

    if (existCustomer && !customer.getId().equals(id)) {
      throw new EntityAlreadyExistsException(MessageFormat
          .format("customer with email {0}", email));
    }
  }
}
