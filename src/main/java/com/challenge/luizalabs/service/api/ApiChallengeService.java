package com.challenge.luizalabs.service.api;

import com.challenge.luizalabs.exception.EntityNotFoundException;
import com.challenge.luizalabs.exception.InternalServerErrorException;
import com.challenge.luizalabs.v1.dto.ProductDto;

import java.text.MessageFormat;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiChallengeService {

  @Value("${api.challenge.product.url}")
  private String baseUrl;

  @Autowired
  private RestTemplate restTemplate;

  /**
   * Method responsible to call api challenge and return product by id.
   *
   * @param productId {UUID}
   * @return ProductDto
   */
  public ProductDto getProductById(final UUID productId) {
    ResponseEntity<ProductDto> productResponseBody;

    try {
      productResponseBody = restTemplate.exchange(baseUrl.concat(productId.toString()).concat("/"),
          HttpMethod.GET,
          this.getHttpEntity(),
          ProductDto.class);
      return productResponseBody.getBody();
    } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
      if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
        throw new EntityNotFoundException(MessageFormat.format("Product {0}", productId));
      }
      throw new InternalServerErrorException();
    }
  }

  private HttpEntity<String> getHttpEntity() {
    HttpHeaders headers = new HttpHeaders();
    return new HttpEntity<>(headers);
  }
}
