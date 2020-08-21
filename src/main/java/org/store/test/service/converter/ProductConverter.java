package org.store.test.service.converter;

import org.store.test.dto.ProductDto;
import org.store.test.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductConverter {
    public Product convert(ProductDto productDto) {
        return new Product(productDto.getId(), productDto.getName(), productDto.getCost());
    }

    public ProductDto convert(Product oProduct) {
        return new ProductDto(oProduct.getId(), oProduct.getName(), oProduct.getCost());
    }

    public List<ProductDto> convert(List<Product> products) {
        return products.stream()
                .map(product -> new ProductDto(product.getId(), product.getName(), product.getCost()))
                .collect(Collectors.toList());
    }
}
