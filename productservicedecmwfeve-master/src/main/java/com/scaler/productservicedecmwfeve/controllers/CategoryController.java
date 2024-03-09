package com.scaler.productservicedecmwfeve.controllers;

import com.scaler.productservicedecmwfeve.exceptions.CategoryIsNullException;
import com.scaler.productservicedecmwfeve.exceptions.CategoryNotExistException;
import com.scaler.productservicedecmwfeve.models.Category;
import com.scaler.productservicedecmwfeve.models.Product;
import com.scaler.productservicedecmwfeve.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/products")
public class CategoryController {
    private ProductService productService;
    private RestTemplate restTemplate;

    public CategoryController(@Qualifier("selfProductService") ProductService productService, RestTemplate restTemplate) {
        this.productService = productService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/category/{catType}")
    public List<Product> getSpecificCategoryProduct(@PathVariable("catType")String catType) throws CategoryNotExistException, CategoryIsNullException {
        return productService.getSpecificCategoryProduct(catType);
    }


    @GetMapping("/categories")
    public List<String> getAllCategories() throws CategoryNotExistException {
        return productService.getAllCategories();
    }
}
