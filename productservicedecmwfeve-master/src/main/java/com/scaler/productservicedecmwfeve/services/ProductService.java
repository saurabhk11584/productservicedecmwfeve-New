package com.scaler.productservicedecmwfeve.services;

import com.scaler.productservicedecmwfeve.dtos.FakeStoreProductDto;
import com.scaler.productservicedecmwfeve.exceptions.CategoryIsNullException;
import com.scaler.productservicedecmwfeve.exceptions.CategoryNotExistException;
import com.scaler.productservicedecmwfeve.exceptions.ProductNotExistsException;
import com.scaler.productservicedecmwfeve.models.Category;
import com.scaler.productservicedecmwfeve.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    Product getSingleProduct(Long id) throws ProductNotExistsException;

    Page<Product> getAllProducts(int pageNumber, int pageSize, String sortBy, String order) throws ProductNotExistsException;

    Product updateProduct(Long id, Product product);

    Product replaceProduct(Long id, Product product) throws ProductNotExistsException;

    Product addNewProduct(Product product);

    Boolean deleteProduct(Long id) throws ProductNotExistsException;

    List<Product> getSpecificCategoryProduct(String catType) throws CategoryNotExistException, CategoryIsNullException;
    List<String> getAllCategories() throws CategoryNotExistException;

}
