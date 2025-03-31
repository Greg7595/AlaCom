package org.example.alacom.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.alacom.domain.mapper.Mapper;
import org.example.alacom.domain.dto.ProductResponse;
import org.example.alacom.domain.dto.ProductPriceRequest;
import org.example.alacom.domain.dto.ProductPriceResponse;
import org.example.alacom.infrastructure.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final DiscountProvider discountProvider;
    private final ProductRepository productRepository;
    private final Mapper mapper ;

    public ProductResponse getProduct(UUID id) {
        return productRepository.findById(id)
                .map(mapper::toProductResponse)
                .orElseThrow();
    }

    public ProductPriceResponse calculatePrice(UUID id, ProductPriceRequest productPriceRequest) {

        var product = productRepository.findById(id).orElseThrow();

        var priceBeforeDiscount = calculatePriceBeforeDiscount(product.getPrice(), productPriceRequest.getQuantity());
        var discount = calculateDiscount(priceBeforeDiscount, productPriceRequest.getQuantity());
        var finalPrice = priceBeforeDiscount.subtract(discount);

        return mapper.toProductPriceResponse(product, finalPrice, discount, productPriceRequest.getQuantity());
    }

    private BigDecimal calculatePriceBeforeDiscount(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateDiscount(BigDecimal priceBeforeDiscount, int quantity) {
        var discountRate = discountProvider.getDiscountRate(quantity);
        return priceBeforeDiscount.multiply(discountRate)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
