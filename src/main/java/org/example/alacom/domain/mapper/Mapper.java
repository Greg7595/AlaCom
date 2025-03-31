package org.example.alacom.domain.mapper;

import org.example.alacom.infrastructure.entity.Product;
import org.example.alacom.domain.dto.ProductResponse;
import org.example.alacom.domain.dto.ProductPriceResponse;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    ProductResponse toProductResponse(Product product);

    @Mapping(target = "unitPrice", source = "product.price")
    ProductPriceResponse toProductPriceResponse(Product product, BigDecimal finalPrice, BigDecimal discount, Integer quantity);
}
