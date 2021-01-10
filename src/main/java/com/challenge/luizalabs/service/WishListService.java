package com.challenge.luizalabs.service;

import com.challenge.luizalabs.exception.EntityAlreadyExistsException;
import com.challenge.luizalabs.exception.EntityNotFoundException;
import com.challenge.luizalabs.exception.InternalServerErrorException;
import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.model.Product;
import com.challenge.luizalabs.model.WishList;
import com.challenge.luizalabs.repository.ProductRepository;
import com.challenge.luizalabs.repository.WishListRepository;
import com.challenge.luizalabs.v1.dto.CustomerDtoResponse;
import com.challenge.luizalabs.v1.dto.ProductDto;
import com.challenge.luizalabs.v1.dto.WishListDtoResponse;

import java.util.ArrayList;
import java.util.List;
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

  /**
   * Method responsible to save a wish list by customer id and product id.
   *
   * @param customerId {Long}
   * @param productId  {UUID}
   * @return WishListDtoResponse
   */
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

        wishListDtoResponse = getProductsByCustomerId(customerId, productApi);
      }
    } catch (InternalServerErrorException e) {
      throw new InternalServerErrorException("Failed fetch api");
    }

    return wishListDtoResponse;
  }

  /**
   * Method responsible to get a wish list by customer id.
   *
   * @param customerId {Long}
   * @return WishListDtoResponse
   */
  public WishListDtoResponse getProductsByCustomerId(final Long customerId,
                                                     final ProductDto productDto) {
    final List<WishList> wishList = getWishLists(customerId);

    List<WishListDtoResponse.WishListProduct> products = new ArrayList<>();

    wishList.forEach(wl -> {
      final Product product = Optional.ofNullable(wl)
          .map(WishList::getProduct)
          .orElse(null);
      final UUID productId = Optional.ofNullable(product)
          .map(Product::getId)
          .orElse(null);

      try {
        ProductDto productApi = productDto;
        if (Optional.ofNullable(productDto).isEmpty()) {
          productApi = productService.getProductApi(productId);
        }

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

  /**
   * Method responsible to delete a wish list by customer id.
   *
   * @param customerId {Long}
   */
  public void delete(final Long customerId) {
    if (!getWishLists(customerId).isEmpty()) {
      this.wishListRepository.deleteByCustomerId(customerId);
    }
  }

  private List<WishList> getWishLists(Long customerId) {
    final List<WishList> wishList = this.wishListRepository.findAllByCustomerId(customerId);

    if (wishList.isEmpty()) {
      throw new EntityNotFoundException("Wishlist");
    }
    return wishList;
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
      throw new EntityAlreadyExistsException("Wish list, but this product and this customer");
    }
  }
}
