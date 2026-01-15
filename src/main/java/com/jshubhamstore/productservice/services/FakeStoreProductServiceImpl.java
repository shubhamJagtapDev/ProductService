package com.jshubhamstore.productservice.services;

import com.jshubhamstore.productservice.dtos.FakeStoreProductDto;
import com.jshubhamstore.productservice.exceptions.ProductNotFoundException;
import com.jshubhamstore.productservice.models.Category;
import com.jshubhamstore.productservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductServiceImpl implements IProductService {
    private RestTemplate restTemplate;
    private static final String FAKE_STORE_PRODUCTS_URL="https://fakestoreapi.com/products/";

    @Autowired
    public FakeStoreProductServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Product> getAllProducts() {
        ResponseEntity<List<FakeStoreProductDto>> responseEntity = restTemplate.exchange(
                FAKE_STORE_PRODUCTS_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FakeStoreProductDto>>() {
                });
        // Using simple array og FakeStoreProductDTO accept the response
//        ResponseEntity<FakeStoreProductDto[]> responseEntity1 = restTemplate.getForEntity(
//                FAKE_STORE_PRODUCTS_URL, FakeStoreProductDto[].class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            assert responseEntity.getBody() != null;
            List<Product> products = new ArrayList<>();
            for(FakeStoreProductDto fakeStoreProductDto : responseEntity.getBody()) {
                products.add(convertFakeStoreProductDtoToProduct(fakeStoreProductDto));
            }
            return products;
        }else {
            return List.of();
        }
    }

    // Make a Get Http call to https://fakestoreapi.com/products/{id}
    // to get a product for the given id
    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate
                .getForEntity(FAKE_STORE_PRODUCTS_URL + productId, FakeStoreProductDto.class);
        if(responseEntity.getBody()==null) throw new ProductNotFoundException(productId);
        return convertFakeStoreProductDtoToProduct(responseEntity.getBody());
    }

    private Product convertFakeStoreProductDtoToProduct(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setTitle(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }
}

