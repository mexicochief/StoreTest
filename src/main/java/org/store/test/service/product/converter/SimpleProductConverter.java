package org.store.test.service.product.converter;

import org.store.test.dto.ProductDto;
import org.store.test.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleProductConverter implements ProductConverter{
    @Override
    public Product convert(ProductDto productDto) {
        return new Product(productDto.getId(), productDto.getName(), productDto.getCost());
    }

    @Override
    public ProductDto convert(Product oProduct) {
        return new ProductDto(oProduct.getId(), oProduct.getName(), oProduct.getCost());
    }

    @Override
    public List<ProductDto> convert(List<Product> products) {
        return products.stream()
                .map(product -> new ProductDto(product.getId(), product.getName(), product.getCost()))
                .collect(Collectors.toList());
    }
}
