package com.challenge.luizalabs.service;

import com.challenge.luizalabs.exception.InternalServerErrorException;
import com.challenge.luizalabs.service.api.ApiChallengeService;
import com.challenge.luizalabs.v1.dto.ProductDto;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  @Value("${api.retry.maxAttempts}")
  private int maxAttempts;

  @Value("${api.retry.waitDurationMilliseconds}")
  private long waitDuration;

  @Autowired
  private ApiChallengeService apiChallengeService;

  /**
   * Method responsible to get a product by id calling a api (this method has a retry).
   *
   * @param productId {UUID}
   * @return ProductDto
   */
  public ProductDto getProductApi(final UUID productId) {
    try {
      Supplier<ProductDto> productDtoCheckedFunction0;
      RetryConfig retryConfig = RetryConfig.custom()
          .maxAttempts(maxAttempts)
          .waitDuration(Duration.ofMillis(waitDuration))
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
