package com.challenge.luizalabs.service.mapper;

import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.v1.dto.CustomerDtoResponse;
import io.github.benas.randombeans.EnhancedRandomBuilder;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerMapperTest {

  @InjectMocks
  private CustomerMapper mapper;

  private EnhancedRandomBuilder enhancedRandom;

  @Before
  public void before() {
    enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
        .collectionSizeRange(1, 1)
        .seed(ThreadLocalRandom.current()
            .nextLong(100));
  }

  @Test
  public void serializeList() {
    List<Customer> customerList = enhancedRandom.build().objects(Customer.class, 1)
        .collect(Collectors.toList());

    Customer customer = customerList.get(0);
    CustomerDtoResponse customerDtoResponse = mapper.serialize(customer);

    Assert.assertEquals(customer.getId(), customerDtoResponse.getId());
    Assert.assertEquals(customer.getEmail(), customerDtoResponse.getEmail());
    Assert.assertEquals(customer.getName(), customerDtoResponse.getName());
  }

}
