package com.challenge.luizalabs.v1.controller;

import com.challenge.luizalabs.service.CustomerService;
import com.challenge.luizalabs.v1.dto.CustomerDtoRequest;
import com.challenge.luizalabs.v1.dto.CustomerDtoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customer")
@Api(value = "Customer")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  /**
   * Method responsible to return all customers.
   *
   * @return {ResponseEntity}
   */
  @GetMapping
  @ApiOperation(value = "Get all customers", response = CustomerDtoResponse.class,
      authorizations = @Authorization(value = "Bearer"))
  public ResponseEntity<List<?>> getAllCustomers() {
    final List<CustomerDtoResponse> customerDtoList = this.customerService.getAll();
    return ResponseEntity.ok(customerDtoList);
  }

  /**
   * Method responsible to return only one customer by id.
   *
   * @param id {Long} customer id
   * @return {ResponseEntity}
   */
  @GetMapping(value = "{id}")
  @ApiOperation(value = "Get customer by id", response = CustomerDtoResponse.class,
      authorizations = @Authorization(value = "Bearer"))
  public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
    final CustomerDtoResponse customerDto = this.customerService.getById(id);
    return ResponseEntity.ok(customerDto);
  }

  /**
   * Method responsible to insert new customer (it isn't possible to create two customers
   * with the same e-mail).
   *
   * @param customer {CustomerDtoRequest}
   * @return {ResponseEntity}
   */
  @PostMapping
  @ApiOperation(value = "Insert customer", response = CustomerDtoResponse.class,
      authorizations = @Authorization(value = "Bearer"))
  public ResponseEntity<?> insertCustomer(@Valid @RequestBody CustomerDtoRequest customer) {
    final CustomerDtoResponse customerDtoResponse =
        this.customerService.saveOrUpdate(customer, null);
    return new ResponseEntity<>(customerDtoResponse, HttpStatus.CREATED);
  }

  /**
   * Method responsible to update a customer.
   *
   * @param id                 {Long} customer id
   * @param customer {CustomerDtoRequest}
   * @return {ResponseEntity}
   */
  @PutMapping(value = "{id}")
  @ApiOperation(value = "Update customer", response = CustomerDtoResponse.class,
      authorizations = @Authorization(value = "Bearer"))
  public ResponseEntity<?> updateCustomer(@PathVariable Long id,
                                          @Valid @RequestBody CustomerDtoRequest customer) {
    final CustomerDtoResponse customerDtoResponse = this.customerService.saveOrUpdate(customer, id);
    return new ResponseEntity<>(customerDtoResponse, HttpStatus.OK);
  }

  /**
   * Method responsible to delete a customer by id.
   *
   * @param id {Long} customer id
   * @return {ResponseEntity}
   */
  @DeleteMapping(value = "{id}")
  @ApiOperation(value = "Delete customer by id", authorizations = @Authorization(value = "Bearer"))
  public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
    this.customerService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
