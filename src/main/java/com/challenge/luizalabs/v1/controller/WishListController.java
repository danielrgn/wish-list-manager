package com.challenge.luizalabs.v1.controller;

import com.challenge.luizalabs.dto.CustomerDtoRequest;
import com.challenge.luizalabs.dto.CustomerDtoResponse;
import com.challenge.luizalabs.dto.ProductDto;
import com.challenge.luizalabs.dto.WishListDtoResponse;
import com.challenge.luizalabs.model.WishList;
import com.challenge.luizalabs.service.CustomerService;
import com.challenge.luizalabs.service.ProductService;
import com.challenge.luizalabs.service.WishListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import java.util.List;
import java.util.UUID;
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
@RequestMapping("/v1/wishlist")
@Api(value="WishList")
public class WishListController {

  @Autowired
  private WishListService wishListService;

  /**
   * Method responsible to return wish list from one customer id.
   *
   * @param customerId {Long} customer id
   * @return {WishListDtoResponse}
   */
  @GetMapping(value = "/customer/{customerId}")
  @ApiOperation(value = "Get wish list by customer id", response = CustomerDtoResponse.class,
      authorizations = @Authorization(value = "Bearer"))
  public ResponseEntity<?> getWishListByCustomerId(@PathVariable Long customerId) {
    final WishListDtoResponse wishListDtoResponse =
        this.wishListService.getProductsByCustomerId(customerId);
    return ResponseEntity.ok(wishListDtoResponse);
  }

  /**
   * Method responsible to insert new wish list by customer id and product id.
   *
   * @param customerId {Long} customer id
   * @param productId {UUID} product id from api challenge
   * @return {WishListDtoResponse}
   */
  @PostMapping(value = "/customer/{customerId}/product/{productId}")
  @ApiOperation(value = "Insert wish list", response = CustomerDtoResponse.class,
      authorizations = @Authorization(value = "Bearer"))
  public ResponseEntity<?> insertWishList(@PathVariable Long customerId,
                                          @PathVariable UUID productId) {
    WishListDtoResponse wishList = this.wishListService.save(customerId, productId);
    return new ResponseEntity<>(wishList, HttpStatus.CREATED);
  }

  /**
   * Method responsible to delete a wish list by customer id.
   *
   * @param customerId {Long} customer id
   * @return {}
   */
  @DeleteMapping(value = "/customer/{customerId}")
  @ApiOperation(value = "Delete wish list by customer id",
      authorizations = @Authorization(value = "Bearer"))
  public ResponseEntity<?> deleteWishListByCustomerId(@PathVariable Long customerId) {
    this.wishListService.delete(customerId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
