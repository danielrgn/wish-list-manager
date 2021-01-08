package com.challenge.luizalabs.service;

import com.challenge.luizalabs.dto.ProductDto;
import com.challenge.luizalabs.exception.InternalServerErrorException;
import com.challenge.luizalabs.service.api.ApiChallengeService;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  @Autowired
  private ApiChallengeService apiChallengeService;

  public ProductDto getProductApi(final UUID productId) {
    try {
      Supplier<ProductDto> productDtoCheckedFunction0;
      RetryConfig retryConfig = RetryConfig.custom()
          .maxAttempts(3)
          .waitDuration(Duration.ofMillis(5000))
          .retryOnException(t -> t instanceof InternalServerErrorException)
          .build();
      Retry retry = Retry.of("retry-call-api-challenge", retryConfig);
      productDtoCheckedFunction0 = () -> apiChallengeService.getProductById(productId);
      Supplier<ProductDto> retryableSupplier =
          Retry.decorateSupplier(retry, productDtoCheckedFunction0);
      return retryableSupplier.get();
    } catch (InternalServerErrorException e) {
      throw new InternalServerErrorException("Retry Failed");
    }
  }
}
