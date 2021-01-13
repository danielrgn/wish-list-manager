package com.challenge.luizalabs.v1.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challenge.luizalabs.commons.ControllerGenericTest;
import com.challenge.luizalabs.factory.CustomerDtoRequestFactory;
import com.challenge.luizalabs.factory.CustomerFactory;
import com.challenge.luizalabs.factory.ProductFactory;
import com.challenge.luizalabs.factory.WishListFactory;
import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.model.WishList;
import com.challenge.luizalabs.repository.CustomerRepository;
import com.challenge.luizalabs.repository.ProductRepository;
import com.challenge.luizalabs.repository.WishListRepository;
import com.challenge.luizalabs.service.api.ChallengeApiWiremock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import java.text.MessageFormat;
import java.util.UUID;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class WishListControllerTest extends ControllerGenericTest {

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(8090);

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private WishListFactory wishListFactory;

  @Autowired
  private CustomerFactory customerFactory;

  @Autowired
  private ProductFactory productFactory;

  @Autowired
  private CustomerDtoRequestFactory customerDtoRequestFactory;

  @Autowired
  private WishListRepository wishListRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private ProductRepository productRepository;

  private MockMvc mockMvc;

  private final String urlPathCustomerResource = MessageFormat.format(
      "{0}{1}", PATH_RESOURCE, "wishlist");

  @Before
  public void before() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    this.customerRepository.deleteAll();
    this.productRepository.deleteAll();
    this.wishListRepository.deleteAll();
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaDeleteWishListByCustomerIdSuccess() {
    WishList wishList = getWishList();
    Long customerId = wishList.getCustomer().getId();

    final String json = this.mockMvc.perform(delete(urlPathCustomerResource.concat("/customer/{customerId}"), customerId.toString())
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isNoContent())
        .andReturn()
        .getResponse()
        .getContentAsString();

    assertTrue(wishListRepository.findAll(Example.of(WishList.builder()
          .customer(Customer.builder()
              .id(customerId)
              .build())
          .build()))
        .isEmpty());
    assertTrue(json.isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaDeleteWishListByCustomerIdWithCustomerWithoutExists() {
    final String json = this.mockMvc.perform(delete(urlPathCustomerResource.concat("/customer/{customerId}"), "1")
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$[0].errorMessage", equalTo("You attempted to get a Wishlist, but did not find any")))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertTrue(wishListRepository.findAll(Example.of(WishList.builder()
        .customer(Customer.builder()
            .id(1L)
            .build())
        .build()))
        .isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaGetWishListByCustomerIdSuccess() {
    ChallengeApiWiremock.setUpChallengeApiSuccess();

    WishList wishList = getWishList();
    Long customerId = wishList.getCustomer().getId();

    final String json = this.mockMvc.perform(get(urlPathCustomerResource.concat("/customer/{customerId}"), customerId.toString())
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-wishlist-response.json");
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaGetWishListByCustomerIdProductNotFound() {
    ChallengeApiWiremock.setUpChallengeApiNotFound();

    WishList wishList = getWishList();
    Long customerId = wishList.getCustomer().getId();
    String productId = wishList.getProduct().getId().toString();

    final String json = this.mockMvc.perform(get(urlPathCustomerResource.concat("/customer/{customerId}"), customerId.toString())
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$[0].errorMessage", equalTo(MessageFormat.format("You attempted to get a Product {0}, but did not find any", productId))))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertEquals(1, wishListRepository.findAll(Example.of(WishList.builder()
        .customer(Customer.builder()
            .id(customerId)
            .build())
        .build()))
        .size());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaGetWishListByCustomerIdCallApiInternalServerError() {
    ChallengeApiWiremock.setUpChallengeApiInternalServerError();

    WishList wishList = getWishList();
    Long customerId = wishList.getCustomer().getId();

    final String json = this.mockMvc.perform(get(urlPathCustomerResource.concat("/customer/{customerId}"), customerId.toString())
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-wishlist-response.json");
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaGetWishListByCustomerIdWithCustomerWithoutExists() {
    final String json = this.mockMvc.perform(get(urlPathCustomerResource.concat("/customer/{customerId}"), "1")
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$[0].errorMessage", equalTo("You attempted to get a Wishlist, but did not find any")))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");

    assertTrue(wishListRepository.findAll(Example.of(WishList.builder()
        .customer(Customer.builder()
            .id(1L)
            .build())
        .build()))
        .isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaInsertWishListSuccess() {
    ChallengeApiWiremock.setUpChallengeApiSuccess();

    Customer customer = customerRepository.save(customerFactory.simple());

    final String json = this.mockMvc.perform(post(urlPathCustomerResource.concat("/customer/{customerId}/product/{productId}"), customer.getId().toString(), UUID.randomUUID())
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-wishlist-response.json");

    assertTrue(!wishListRepository.findAll(Example.of(WishList.builder()
        .customer(Customer.builder()
            .id(customer.getId())
            .build())
        .build()))
        .isEmpty());
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaInsertWishListWithCustomerIdAndProductIdAlreadyExists() {
    ChallengeApiWiremock.setUpChallengeApiSuccess();

    WishList wishList = getWishList();
    Long customerId = wishList.getCustomer().getId();
    String productId = wishList.getProduct().getId().toString();

    final String json = this.mockMvc.perform(post(urlPathCustomerResource.concat("/customer/{customerId}/product/{productId}"), customerId.toString(), productId)
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$[0].errorMessage", equalTo("You attempted to create Wish list, but this product and this customer already exists")))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaInsertWishListWithProductIdInvalid() {
    ChallengeApiWiremock.setUpChallengeApiSuccess();

    WishList wishList = getWishList();
    Long customerId = wishList.getCustomer().getId();

    final String json = this.mockMvc.perform(post(urlPathCustomerResource.concat("/customer/{customerId}/product/{productId}"), customerId.toString(), "a")
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].errorMessage", equalTo("Invalid field productId - it must be filled with a valid UUID")))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");
  }

  @Test
  @Rollback
  @SneakyThrows
  public void validateSchemaInsertWishListWithApiChallengeResponseInternalServerError() {
    ChallengeApiWiremock.setUpChallengeApiInternalServerError();

    WishList wishList = getWishList();
    Long customerId = wishList.getCustomer().getId();

    final String json = this.mockMvc.perform(post(urlPathCustomerResource.concat("/customer/{customerId}/product/{productId}"), customerId.toString(), UUID.randomUUID())
        .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$[0].errorMessage", equalTo("Was encountered an error when processing your request. We apologize for the inconvenience.")))
        .andReturn()
        .getResponse()
        .getContentAsString();
    super.validateSchema(json, "/schema.v1/controller/schema-error-response.json");
  }

  private WishList getWishList() {
    WishList wishList = wishListFactory.simple();
    wishList.setCustomer(customerRepository.save(customerFactory.simple()));
    wishList.setProduct(productRepository.save(productFactory.simple()));

    return wishListRepository.save(wishList);
  }
}