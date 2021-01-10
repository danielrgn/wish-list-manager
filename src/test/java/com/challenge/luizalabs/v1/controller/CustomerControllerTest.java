package com.challenge.luizalabs.v1.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challenge.luizalabs.commons.ControllerGenericTest;
import com.challenge.luizalabs.factory.CustomerDtoRequestFactory;
import com.challenge.luizalabs.factory.CustomerFactory;
import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.repository.CustomerRepository;
import com.challenge.luizalabs.v1.dto.CustomerDtoRequest;

import java.text.MessageFormat;
import java.util.Optional;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class CustomerControllerTest extends ControllerGenericTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private CustomerFactory customerFactory;

  @Autowired
  private CustomerDtoRequestFactory customerDtoRequestFactory;

  @Autowired
  private CustomerRepository customerRepository;

  private MockMvc mockMvc;

  private final String urlPathCustomerResource = MessageFormat.format(
      "{0}{1}", PATH_RESOURCE, "customer");

  @Before
  public void before() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    this.customerRepository.deleteAll();
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaDeleteCustomerSuccess() {
    Customer customer = customerRepository.save(customerFactory.simple());

    final String json = this.mockMvc.perform(delete(urlPathCustomerResource.concat("/{id}"), customer.getId().toString())
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isNoContent())
        .andReturn()
        .getResponse()
        .getContentAsString();

    assertTrue(customerRepository.findAll(Example.of(Customer.builder()
        .id(customer.getId()).build()))
        .isEmpty());
    assertTrue(json.isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaDeleteWithCustomerNotExist() {
    final String json = this.mockMvc.perform(delete(urlPathCustomerResource.concat("/{id}"), "1")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$[0].developerMessage", equalTo("No class com.challenge.luizalabs.model.Customer entity with id 1 exists!")))
        .andExpect(jsonPath("$[0].userMessage", equalTo("We not found this entity with the id requested. We apologize for the inconvenience.")))
        .andExpect(jsonPath("$[0].errorCode", equalTo(55500)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertTrue(customerRepository.findAll(Example.of(Customer.builder()
        .id(1L).build()))
        .isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaGetCustomerByIdSuccess() {
    Customer customer = customerRepository.save(customerFactory.simple());

    final String json = this.mockMvc.perform(get(urlPathCustomerResource.concat("/{id}"), customer.getId().toString())
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-customer-response.json");
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaGetCustomerByIdNotExist() {
    final String json = this.mockMvc.perform(get(urlPathCustomerResource.concat("/{id}"), "1")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$[0].developerMessage", equalTo("Customer not found")))
        .andExpect(jsonPath("$[0].userMessage", equalTo("You attempted to get a Customer, but did not find any")))
        .andExpect(jsonPath("$[0].errorCode", equalTo(20023)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertTrue(customerRepository.findAll(Example.of(Customer.builder()
        .id(1L).build()))
        .isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaGetAllCustomerSuccess() {
    customerRepository.save(customerFactory.simple());

    final String json = this.mockMvc.perform(get(urlPathCustomerResource)
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-list-customer-response.json");
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaGetAllCustomersNotExist() {
    final String json = this.mockMvc.perform(get(urlPathCustomerResource)
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$[0].developerMessage", equalTo("Customer not found")))
        .andExpect(jsonPath("$[0].userMessage", equalTo("You attempted to get a Customer, but did not find any")))
        .andExpect(jsonPath("$[0].errorCode", equalTo(20023)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertTrue(customerRepository.findAll(Example.of(Customer.builder()
        .id(1L).build()))
        .isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaInsertCustomerSuccess() {
    final String json = this.mockMvc.perform(post(urlPathCustomerResource)
        .content(getJsonMapped(customerDtoRequestFactory.simple()))
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-customer-response.json");
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaInsertCustomerWithEmailInvalid() {
    CustomerDtoRequest customerDtoRequest = customerDtoRequestFactory.simple();
    customerDtoRequest.setEmail("email");

    final String json = this.mockMvc.perform(post(urlPathCustomerResource)
        .content(getJsonMapped(customerDtoRequest))
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].developerMessage", equalTo("Invalid body parameter email - it must be filled with a valid email")))
        .andExpect(jsonPath("$[0].userMessage", equalTo("Invalid field email - it must be filled with a valid email")))
        .andExpect(jsonPath("$[0].errorCode", equalTo(20013)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertTrue(customerRepository.findAll(Example.of(Customer.builder()
        .id(1L).build()))
        .isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaInsertCustomerDtoRequestWithNameIsNull() {
    CustomerDtoRequest customerDtoRequest = customerDtoRequestFactory.simple();
    customerDtoRequest.setName(null);

    final String json = this.mockMvc.perform(post(urlPathCustomerResource)
        .content(getJsonMapped(customerDtoRequest))
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].developerMessage", equalTo("Missing body parameter name")))
        .andExpect(jsonPath("$[0].userMessage", equalTo("Field name is required and can not be empty")))
        .andExpect(jsonPath("$[0].errorCode", equalTo(20001)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertTrue(customerRepository.findAll(Example.of(Customer.builder()
        .id(1L).build()))
        .isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaInsertCustomerDtoRequestWithEmailIsNull() {
    CustomerDtoRequest customerDtoRequest = customerDtoRequestFactory.simple();
    customerDtoRequest.setEmail(null);

    final String json = this.mockMvc.perform(post(urlPathCustomerResource)
        .content(getJsonMapped(customerDtoRequest))
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].developerMessage", equalTo("Missing body parameter email")))
        .andExpect(jsonPath("$[0].userMessage", equalTo("Field email is required and can not be empty")))
        .andExpect(jsonPath("$[0].errorCode", equalTo(20001)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertTrue(customerRepository.findAll(Example.of(Customer.builder()
        .id(1L).build()))
        .isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaInsertCustomerWithEmailDuplicated() {
    CustomerDtoRequest customerDtoRequest = customerDtoRequestFactory.simple();
    Customer customer = customerFactory.simple();

    customer.setEmail(customerDtoRequest.getEmail());
    Customer customerSaved = customerRepository.save(customer);

    final String json = this.mockMvc.perform(post(urlPathCustomerResource)
        .content(getJsonMapped(customerDtoRequest))
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$[0].developerMessage", equalTo(MessageFormat.format("Customer with email {0} already exists", customer.getEmail()))))
        .andExpect(jsonPath("$[0].userMessage", equalTo(MessageFormat.format("You attempted to create Customer with email {0} already exists", customer.getEmail()))))
        .andExpect(jsonPath("$[0].errorCode", equalTo(20033)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertEquals(1, customerRepository.findAll(Example.of(Customer.builder()
        .id(customerSaved.getId()).build()))
        .size());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaUpdateCustomerSuccess() {
    Customer customer = customerRepository.save(customerFactory.simple());

    CustomerDtoRequest customerDtoRequest = CustomerDtoRequest.builder()
          .email("email@hotmail.com")
          .name("Teste")
        .build();

    final String json = this.mockMvc.perform(put(urlPathCustomerResource.concat("/{id}"), customer.getId())
        .content(getJsonMapped(customerDtoRequest))
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-customer-response.json");

    Optional<Customer> customerById = customerRepository.findById(customer.getId());
    assertTrue(!customerById.isEmpty());
    assertEquals(customerDtoRequest.getName(), customerById.get().getName());
    assertEquals(customerDtoRequest.getEmail(), customerById.get().getEmail());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaUpdateCustomerWithoutParamId() {
    final String json = this.mockMvc.perform(put(urlPathCustomerResource)
        .content(getJsonMapped(customerDtoRequestFactory.simple()))
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isMethodNotAllowed())
        .andExpect(jsonPath("$[0].developerMessage", equalTo("Method not allowed")))
        .andExpect(jsonPath("$[0].userMessage", equalTo("Method not allowed")))
        .andExpect(jsonPath("$[0].errorCode", equalTo(20021)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaDeleteWithParamIdInvalid() {
    final String json = this.mockMvc.perform(delete(urlPathCustomerResource.concat("/{id}"), "a")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].developerMessage", equalTo("Invalid query parameter id=a - it is not allowed")))
        .andExpect(jsonPath("$[0].userMessage", equalTo("Invalid field id - it is not allowed")))
        .andExpect(jsonPath("$[0].errorCode", equalTo(20035)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");
  }
}