package com.erp.product.model.repository;

import com.erp.product.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, String>  {

    List<ProductEntity> getAllProducts();

     Optional<ProductEntity> getProductById(String id);

    ProductEntity saveProduct(ProductEntity productEntity);

    void deleteProduct(String id);
}
