package com.challenge.luizalabs.service;

import com.challenge.luizalabs.dto.CustomerDtoResponse;
import com.challenge.luizalabs.dto.ProductDto;
import com.challenge.luizalabs.dto.WishListDtoResponse;
import com.challenge.luizalabs.exception.EntityAlreadyExistsException;
import com.challenge.luizalabs.exception.EntityNotFoundException;
import com.challenge.luizalabs.exception.InternalServerErrorException;
import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.model.Product;
import com.challenge.luizalabs.model.WishList;
import com.challenge.luizalabs.repository.ProductRepository;
import com.challenge.luizalabs.repository.WishListRepository;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WishListService {

  @Value("${api.challenge.product.url}")
  private String baseUrl;

  @Autowired
  private ProductService productService;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private WishListRepository wishListRepository;

  @Autowired
  private ProductRepository productRepository;

  public WishListDtoResponse save(final Long customerId, final UUID productId) {
    validateWishListAlreadyExists(customerId, productId);

    final CustomerDtoResponse customerDto = customerService.getById(customerId);
    WishListDtoResponse wishListDtoResponse = WishListDtoResponse.builder().build();

    try {
      final ProductDto productApi = productService.getProductApi(productId);

      if (Optional.ofNullable(productApi).map(ProductDto::getId).isPresent()
          && Optional.ofNullable(customerDto).map(CustomerDtoResponse::getId).isPresent()) {
        final Product product = saveProduct(productApi);

        WishList wishList = WishList.builder()
            .customer(Customer.builder().id(customerId).build())
            .product(product)
            .build();

        wishListRepository.save(wishList);

        wishListDtoResponse = getProductsByCustomerId(customerId);
      }
    } catch (InternalServerErrorException e) {
      throw new InternalServerErrorException("Failed fetch api");
    }

    return wishListDtoResponse;
  }

  public WishListDtoResponse getProductsByCustomerId(final Long customerId) {
    final List<WishList> wishList = this.wishListRepository.findAllByCustomerId(customerId);

    if (wishList.isEmpty()) {
      throw new EntityNotFoundException("Wishlist");
    }

    List<WishListDtoResponse.WishListProduct> products = new ArrayList<>();

    wishList.forEach(wl -> {
      final Product product = Optional.ofNullable(wl)
          .map(WishList::getProduct)
          .orElse(null);
      final UUID productId = Optional.ofNullable(product)
          .map(Product::getId)
          .orElse(null);

      try {
        ProductDto productApi = productService.getProductApi(productId);

        Optional.ofNullable(productApi).ifPresent(p ->
            products.add(WishListDtoResponse.WishListProduct.builder()
              .title(p.getTitle())
              .price(p.getPrice())
              .image(p.getImage())
              .reviewScore(p.getReviewScore())
              .link(baseUrl.concat(p.getId().toString()).concat("/"))
              .build()));
      } catch (InternalServerErrorException e) {
        Optional.ofNullable(product).ifPresent(p ->
          products.add(WishListDtoResponse.WishListProduct.builder()
              .title(product.getTitle())
              .price(product.getPrice())
              .image(product.getImage())
              .reviewScore(product.getReviewScore())
              .link(baseUrl.concat(product.getId().toString()).concat("/"))
              .build()));
      }
    });

    return WishListDtoResponse.builder()
        .customerId(customerId)
        .wishListProducts(products)
        .build();
  }

  public void delete(final Long customerId) {
    final CustomerDtoResponse customerDto = customerService.getById(customerId);
    if (Optional.ofNullable(customerDto).map(CustomerDtoResponse::getId).isPresent()) {
      this.wishListRepository.deleteByCustomerId(customerId);
    }
  }

  private Product saveProduct(@NonNull final ProductDto productApi) {
    final Product product = Product.builder()
        .id(productApi.getId())
        .brand(productApi.getBrand())
        .title(productApi.getTitle())
        .image(productApi.getImage())
        .price(productApi.getPrice())
        .reviewScore(productApi.getReviewScore())
        .build();

    productRepository.save(product);
    return product;
  }

  private void validateWishListAlreadyExists(Long customerId, UUID productId) {
    final boolean alreadyExistProductInWishList =
        wishListRepository.existsWishListByCustomerIdAndProductId(customerId, productId);

    if (alreadyExistProductInWishList) {
      throw new EntityAlreadyExistsException(
          MessageFormat.format("wish list, but this product and this customer", productId));
    }
  }
}
