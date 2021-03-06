package com.challenge.luizalabs.service;

import com.challenge.luizalabs.exception.EntityAlreadyExistsException;
import com.challenge.luizalabs.exception.EntityNotFoundException;
import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.repository.CustomerRepository;
import com.challenge.luizalabs.service.mapper.CustomerMapper;
import com.challenge.luizalabs.v1.dto.CustomerDtoRequest;
import com.challenge.luizalabs.v1.dto.CustomerDtoResponse;

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

  /**
   * Method responsible to save or update a customer.
   *
   * @param customerDtoRequest {CustomerDtoRequest}
   * @param id                 {Long}
   * @return CustomerDtoResponse
   */
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

    if (Optional.ofNullable(id).isPresent()) {
      customerObj.setId(id);
    }

    final Customer customerSaved = customerRepository.save(customerObj);

    return CustomerMapper.serialize(customerSaved);
  }

  /**
   * Method responsible to get all customers.
   *
   * @return {List<CustomerDtoResponse/>}
   */
  @Override
  public List<CustomerDtoResponse> getAll() {
    final List<CustomerDtoResponse> dto = new ArrayList<>();
    final List<Customer> customers = this.customerRepository.findAll();

    if (customers.isEmpty()) {
      throw new EntityNotFoundException("Customer");
    }

    customers.forEach(c -> dto.add(CustomerMapper.serialize(c)));

    return dto;
  }

  /**
   * Method responsible to get a customer by id.
   *
   * @param id {Long}
   * @return CustomerDtoResponse
   */
  @Override
  public CustomerDtoResponse getById(final Long id) {
    final Optional<Customer> customer = this.customerRepository.findById(id);
    if (customer.isPresent()) {
      return CustomerMapper.serialize(customer.get());
    }
    throw new EntityNotFoundException("Customer");
  }

  /**
   * Method responsible delete a customer by id.
   *
   * @param id {Long}
   */
  @Override
  public void delete(final Long id) {
    this.customerRepository.deleteById(id);
  }

  private void validateCustomerExistsByEmail(final String email, final Long id) {
    final Customer customer = customerRepository.findByEmail(email);
    final boolean existCustomer = Optional.ofNullable(customer).isPresent();

    if (existCustomer && !customer.getId().equals(id)) {
      throw new EntityAlreadyExistsException(MessageFormat
          .format("Customer with email {0}", email));
    }
  }
}
