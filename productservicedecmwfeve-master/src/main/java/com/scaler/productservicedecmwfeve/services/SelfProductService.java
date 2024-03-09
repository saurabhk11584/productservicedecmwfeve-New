package com.scaler.productservicedecmwfeve.services;

import com.scaler.productservicedecmwfeve.exceptions.CategoryIsNullException;
import com.scaler.productservicedecmwfeve.exceptions.CategoryNotExistException;
import com.scaler.productservicedecmwfeve.exceptions.ProductNotExistsException;
import com.scaler.productservicedecmwfeve.models.Category;
import com.scaler.productservicedecmwfeve.models.Product;
import com.scaler.productservicedecmwfeve.repositories.CategoryRepository;
import com.scaler.productservicedecmwfeve.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Primary
@Service("selfProductService")
public class SelfProductService implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public SelfProductService(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long id) throws ProductNotExistsException { // In Class

        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new ProductNotExistsException("Product with id: " + id + " doesn't exist.");
        }

        Product product = productOptional.get();

        return product;
    }

    @Override
    public Page<Product> getAllProducts(int pageNumber, int sizeOfPage, String sortBy, String order) throws ProductNotExistsException {

        Sort sort = Sort.by("price").descending().and(Sort.by("name").ascending());

        Optional<Page<Product>> productOptional = Optional.ofNullable(productRepository.findAll(PageRequest.of(
                pageNumber, sizeOfPage, sort
        )));
        if(productOptional.isEmpty()) {
            throw new ProductNotExistsException("Product are not present");
        }
        Page<Product> productList = productOptional.get();
        return productList;
    }

    @Override
    public Product updateProduct(Long id, Product product) { // In Class
        Optional<Category> categoryOptional = categoryRepository.findByName(product.getCategory().getName());
        if(categoryOptional.isEmpty())
            categoryRepository.save(product.getCategory());
        Optional<Product> existingProduct = productRepository.findById(id);
        BeanUtils.copyProperties(product, existingProduct.get(), "id");
        productRepository.save(existingProduct.get());
        return existingProduct.get();
    }

    @Override
    public Product replaceProduct(Long id, Product product) throws ProductNotExistsException {
        Optional<Product> existingProduct = productRepository.findById(id);
        if(existingProduct.isEmpty()) {
            throw new ProductNotExistsException("Product with id: " + id + " doesn't exist.");
        }
        product.setId(existingProduct.get().getId());

        productRepository.save(product);
        return product;
    }

    @Override
    public Product addNewProduct(Product product) { // In Class

        Optional<Category> categoryOptional = categoryRepository.findByName(product.getCategory().getName());

        // If that category is not present, then create a new category and save it in Database of category
        if (categoryOptional.isEmpty()) {
            product.setCategory(categoryRepository.save(product.getCategory()));
        } else {
            product.setCategory(categoryOptional.get());
        }

        return productRepository.save(product);
    }

    @Override
    public Boolean deleteProduct(Long id) throws ProductNotExistsException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()) {
            throw new ProductNotExistsException("Product does not exist");
        }
        productRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Product> getSpecificCategoryProduct(String catType) throws CategoryNotExistException, CategoryIsNullException {
        Optional<Category> categoryOptional = categoryRepository.findByName(catType);
        if(categoryOptional.isEmpty()) {
            throw new CategoryNotExistException("Category does not exist");
        }
        Optional<List<Product>> productList = Optional.ofNullable(productRepository.findByCategory(categoryOptional.get()));
        if(productList.isEmpty()) {
            throw new CategoryIsNullException("Category does not contain any products");
        }
        return productList.get();
    }

    @Override
    public List<String> getAllCategories() throws CategoryNotExistException {
        Optional<List<Category>> optionalCategories = Optional.of(categoryRepository.findAll());
        if(optionalCategories.isEmpty()) {
            throw new CategoryNotExistException("Categories does not exists");
        }
        List<String> categories = new ArrayList<>();
        List<Category> categoryList = optionalCategories.get();
        for(Category category:categoryList) {
            categories.add(category.getName());
        }
        return categories;
    }
}
