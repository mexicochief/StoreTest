package org.store.test.service.product;

import org.store.test.dto.ProductDto;
import org.store.test.exception.EntityNotFoundException;
import org.store.test.model.Product;
import org.store.test.repository.ProductDbManager;
import org.store.test.service.product.converter.ProductConverter;
import org.store.test.service.product.converter.SimpleProductConverter;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductDbManager productDbManager;
    private final ProductConverter productConverter;

    public ProductService(ProductDbManager productDbManager, ProductConverter productConverter) {
        this.productDbManager = productDbManager;
        this.productConverter = productConverter;
    }

    public ProductDto put(ProductDto productDto) {
        final Optional<Product> oProduct = productDbManager.put(productConverter.convert(productDto));
        final Product product = oProduct.orElseThrow(() -> new EntityNotFoundException("Order"));
        return productConverter.convert(product);
    }

    public List<ProductDto> get() {
        return productConverter.convert(productDbManager.get());
    }

    public ProductDto getById(long id) {
        final Product product = productDbManager.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order"));
        return productConverter.convert(product);
    }
}
