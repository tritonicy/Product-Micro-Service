package com.microservices.product_service.Service;

import com.microservices.product_service.Entity.CategoryType;
import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Request.AddProductRequest;
import com.microservices.product_service.Request.CategoryQueryRequest;
import com.microservices.product_service.Response.ProductListResponse;
import com.microservices.product_service.Response.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductListResponse getProducts();

    ProductResponse getProduct(Long id);

    ProductResponse addProduct(AddProductRequest request);

    ProductResponse updateStock(Long id, AddProductRequest request);

    Optional<Product> findById(Long id);

    ProductListResponse searchProducts(String categoryType, Long categoryCode);

}
