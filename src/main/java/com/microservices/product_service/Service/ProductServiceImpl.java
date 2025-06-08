package com.microservices.product_service.Service;

import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.CategoryType;
import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Mappers.ProductMapper;
import com.microservices.product_service.Repository.ProductRepository;
import com.microservices.product_service.Request.AddProductRequest;
import com.microservices.product_service.Request.CategoryQueryRequest;
import com.microservices.product_service.Response.ProductListResponse;
import com.microservices.product_service.Response.ProductResponse;
import com.microservices.product_service.Specs.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final CategoryService categoryService;

    @Override
    public ProductListResponse getProducts() {
        ProductListResponse response = new ProductListResponse();
        response.setProductDTOS(mapper.toDTOList(repository.findAll()));
        return response;
    }

    @Override
    public ProductResponse getProduct(Long id) {
        ProductResponse response = new ProductResponse();
        repository.findById(id)
                .map(mapper::toDTO)
                .ifPresent(response::setProductDTO);

        return response;
    }

    @Override
    public ProductResponse addProduct(AddProductRequest request) {
        ProductResponse response = new ProductResponse();
        Product product = new Product();
        if(request.getName() != null) {
            product.setName(request.getName());
        }
        if(request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if(request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if(request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if(request.getPopularityScore() != null) {
            product.setPopularityScore(request.getPopularityScore());
        }
        if(request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }
        if(request.getCategoryCode() != null) {
            Optional<Category> category = categoryService.getCategoryByCode(request.getCategoryCode());
            if(category.isPresent())
            product.setCategory(category.get());
        }
        else if(request.getCategoryType() != null) {
            CategoryType categoryTypeEnum;
            try {
                categoryTypeEnum = CategoryType.valueOf(request.getCategoryType().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Geçersiz categoryType");
            }
            Optional<Category> category = categoryService.getCategoryByType(categoryTypeEnum);
            if(category.isPresent())
                product.setCategory(category.get());
        }
        Product updatedProduct = repository.save(product);
        response.setProductDTO(mapper.toDTO(updatedProduct));
        return response;
    }

    @Override
    public ProductResponse updateStock(Long id, AddProductRequest request) {
        ProductResponse response = new ProductResponse();
        Optional<Product> optProduct = repository.findById(id);
        if(optProduct.isPresent()) {
            Product product = optProduct.get();
            product.setStockQuantity(request.getStockQuantity());
            Product updatedProduct = repository.save(product);
            response.setProductDTO(mapper.toDTO(updatedProduct));
        }
        return response;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ProductListResponse searchProducts(String categoryType, Long categoryCode) {
        ProductListResponse response = new ProductListResponse();
        if(categoryCode != null) {
            response.setProductDTOS(mapper.toDTOList(repository.findAll(ProductSpecifications.hasCategoryCode(categoryCode))));
        }
        else if (categoryType != null) {
            CategoryType categoryTypeEnum;
            try {
                categoryTypeEnum = CategoryType.valueOf(categoryType.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Geçersiz categoryType: " + categoryType);
            }
            response.setProductDTOS(mapper.toDTOList(repository.findAll(ProductSpecifications.hasCategoryType(categoryTypeEnum))));
        }
        return response;
    }
}
