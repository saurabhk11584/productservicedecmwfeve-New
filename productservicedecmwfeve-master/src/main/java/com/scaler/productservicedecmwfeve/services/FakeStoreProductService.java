package com.scaler.productservicedecmwfeve.services;

import com.scaler.productservicedecmwfeve.dtos.FakeStoreProductDto;
import com.scaler.productservicedecmwfeve.exceptions.ProductNotExistsException;
import com.scaler.productservicedecmwfeve.models.Category;
import com.scaler.productservicedecmwfeve.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {
    private RestTemplate restTemplate;
    private RedisTemplate<String, Objects> redisTemplate;

    @Autowired
    public FakeStoreProductService(RestTemplate restTemplate, RedisTemplate<String, Objects> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    private Product convertFakeStoreProductToProduct(FakeStoreProductDto fakeStoreProduct) {
        Product product = new Product();
        product.setTitle(fakeStoreProduct.getTitle());
        product.setId(fakeStoreProduct.getId());
        product.setPrice(fakeStoreProduct.getPrice());
        product.setDescription(fakeStoreProduct.getDescription());
        product.setImageUrl(fakeStoreProduct.getImage());
        product.setCategory(new Category());
        product.getCategory().setName(fakeStoreProduct.getCategory());

        return product;
    }

    @Override
    public Product getSingleProduct(Long id) throws ProductNotExistsException {
//        int a = 1 / 0;

        Product p = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCT_"+id);
        if(p != null) {
            return p;
        }
        FakeStoreProductDto productDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + id,
                FakeStoreProductDto.class
        );

        if (productDto == null) {
            throw new ProductNotExistsException(
                    "Product with id: " + id + " doesn't exist."
            );
        }
        Product product = convertFakeStoreProductToProduct(productDto);
        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCT_"+id, product);
        return product;
    }

    @Override
    public Page<Product> getAllProducts(int pageNumber, int pageSize, String sortBy, String order) {



//        List<FakeStoreProductDto> response = restTemplate.getForObject(
//                "https://fakestoreapi.com/products",
//                List<FakeStoreProductDto>.class
//        );

        // runtime
        FakeStoreProductDto[] response = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class
        );


        List<Product> answer = new ArrayList<>();


        for (FakeStoreProductDto dto: response) {
            answer.add(convertFakeStoreProductToProduct(dto));
        }

        return new PageImpl<>(answer);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        FakeStoreProductDto updatedDto = restTemplate.exchange(
                "https://fakestoreapi.com/products/"+id,
                HttpMethod.PUT,
                new HttpEntity<>(product),
                FakeStoreProductDto.class,
                id
        ).getBody();
        System.out.println(updatedDto);
        if(updatedDto != null) {
            System.out.println("Product is updated");
            return convertFakeStoreProductToProduct(updatedDto);
        }
        System.out.println("Product is not updated");
        return null;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImage(product.getDescription());
        fakeStoreProductDto.setImage(product.getImageUrl());

        RequestCallback requestCallback = restTemplate.httpEntityCallback(fakeStoreProductDto, FakeStoreProductDto.class);
        HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor =
                new HttpMessageConverterExtractor<>(FakeStoreProductDto.class, restTemplate.getMessageConverters());
        FakeStoreProductDto response = restTemplate.execute("https://fakestoreapi.com/products/" + id, HttpMethod.PUT, requestCallback, responseExtractor);

        return convertFakeStoreProductToProduct(response);
    }

    @Override
    public Product addNewProduct(Product product) {
        if(product == null) {
            return null;
        }
        FakeStoreProductDto productDto = restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                product,
                FakeStoreProductDto.class
        );
        return convertFakeStoreProductToProduct(productDto);
    }

    @Override
    public Boolean deleteProduct(Long id) {
        FakeStoreProductDto fakeStoreProductDto = restTemplate.exchange(
                "https://fakestoreapi.com/products/"+id,
                HttpMethod.DELETE,
                null,
                FakeStoreProductDto.class
        ).getBody();
        if(fakeStoreProductDto != null) {
            System.out.println("Product is deleted");
            return true;
        }
        System.out.println("Product is not found");
        return false;
    }

    @Override
    public List<Product> getSpecificCategoryProduct(String catType) {
        System.out.println(catType);
        FakeStoreProductDto[] fakeStoreProductDtos = restTemplate.getForObject(
                "https://fakestoreapi.com/products/category/" + catType,
                FakeStoreProductDto[].class
        );
        if(fakeStoreProductDtos != null) {
            List<Product> products = new ArrayList<>();
            for(FakeStoreProductDto fakeStoreProductDto:fakeStoreProductDtos) {
                products.add(convertFakeStoreProductToProduct(fakeStoreProductDto));
            }
            return products;
        }
        System.out.println("Products are not there");
        return null;
    }

    @Override
    public List<String> getAllCategories() {
        List<String> categories = List.of(Objects.requireNonNull(restTemplate.getForObject(
                "https://fakestoreapi.com/products/categories",
                String.class
        )));
        return categories;
    }
}


