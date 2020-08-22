package org.store.test.service.product.converter;

import org.store.test.dto.ProductDto;
import org.store.test.model.Product;

import java.util.List;

public interface ProductConverter  {
    Product convert(ProductDto productDto);

    ProductDto convert(Product oProduct);

    List<ProductDto> convert(List<Product> products);
}
