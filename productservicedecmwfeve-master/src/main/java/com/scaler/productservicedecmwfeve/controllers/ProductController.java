package com.scaler.productservicedecmwfeve.controllers;

import com.scaler.productservicedecmwfeve.commons.AuthenticationCommons;
import com.scaler.productservicedecmwfeve.dtos.FakeStoreProductDto;
import com.scaler.productservicedecmwfeve.dtos.Role;
import com.scaler.productservicedecmwfeve.dtos.UserDto;
import com.scaler.productservicedecmwfeve.exceptions.ProductNotExistsException;
import com.scaler.productservicedecmwfeve.models.Product;
import com.scaler.productservicedecmwfeve.services.FakeStoreProductService;
import com.scaler.productservicedecmwfeve.services.ProductService;
import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    private RestTemplate restTemplate;

    private AuthenticationCommons authenticationCommons;

    @Autowired
    public ProductController(@Qualifier("selfProductService") ProductService productService, RestTemplate restTemplate,
                             AuthenticationCommons authenticationCommons) {
        this.productService = productService;
        this.restTemplate = restTemplate;
        this.authenticationCommons = authenticationCommons;
    }

    @GetMapping() // localhost:8080/products
    public ResponseEntity<Page<Product>> getAllProducts(//@RequestHeader("AuthenticationToken") String token,
                                                        @RequestParam("pageNumber") int pageNumber,
                                                        @RequestParam("pageSize") int pageSize,
                                                        @RequestParam("sortBy") String sortBy,
                                                        @RequestParam("sortOrder") String order) throws ProductNotExistsException {
//        restTemplate.delete(null);

//        UserDto userDto = authenticationCommons.validateToken(token);
//
//        if(authenticationCommons.validateToken(token) == null) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//        boolean isAdmin = false;
//
//        for(Role role:userDto.getRoles()) {
//            if(role.getName().equals("ADMIN")) {
//                isAdmin = true;
//                break;
//            }
//        }
//
//        if(!isAdmin) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }

        Page<Product> products = productService.getAllProducts(pageNumber, pageSize, "", "");

        ResponseEntity<Page<Product>> response = new ResponseEntity<>(
                products, HttpStatus.FORBIDDEN
        );

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable("id") Long id) throws ProductNotExistsException {
        return new ResponseEntity<>(
                productService.getSingleProduct(id),
                HttpStatus.OK
        );
    }

    @PostMapping()
    public ResponseEntity<Product> addNewProduct(@RequestBody Product product) {
        return new ResponseEntity<>(
                productService.addNewProduct(product),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        return new ResponseEntity<>(
                productService.updateProduct(id, product),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(@PathVariable("id") Long id, @RequestBody Product product) throws ProductNotExistsException {
        return new ResponseEntity<>(
                productService.replaceProduct(id, product),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") Long id) throws ProductNotExistsException {
        return new ResponseEntity<Boolean>(
                productService.deleteProduct(id),
                HttpStatus.OK
        );
    }

    @ExceptionHandler(ProductNotExistsException.class)
    public ResponseEntity<Void> handleProductNotExistException() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
