package org.example.alacom.unit;

import org.example.alacom.domain.mapper.Mapper;
import org.example.alacom.infrastructure.entity.Product;
import org.example.alacom.domain.dto.ProductPriceRequest;
import org.example.alacom.domain.dto.ProductPriceResponse;
import org.example.alacom.domain.dto.ProductResponse;
import org.example.alacom.domain.service.DiscountProvider;
import org.example.alacom.domain.service.ProductService;
import org.example.alacom.infrastructure.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private DiscountProvider discountProvider;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private Mapper mapper;
    @InjectMocks
    private ProductService productService;

    @Test
    void getProduct() {

        var productId = UUID.randomUUID();

        var mockProduct = createProduct(productId, "ticket", BigDecimal.valueOf(50));
        var mockResponse = ProductResponse
                .builder()
                .id(productId)
                .name("ticket")
                .price(BigDecimal.valueOf(50))
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        Mockito.when(mapper.toProductResponse(mockProduct)).thenReturn(mockResponse);

        var response = productService.getProduct(productId);

        Assertions.assertEquals(mockResponse, response);
    }

    @Test
    void calculatePrice() {

        var productId = UUID.randomUUID();
        var quantity = 6;
        var discountRate = BigDecimal.valueOf(0.1);
        var discountValue = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_UP);
        var finalPrice = BigDecimal.valueOf(270).setScale(2, RoundingMode.HALF_UP);

        var mockProduct = createProduct(productId, "chair", BigDecimal.valueOf(50));
        var mockResponse = ProductPriceResponse
                .builder()
                .id(productId)
                .name("chair")
                .quantity(quantity)
                .unitPrice(BigDecimal.valueOf(50))
                .finalPrice(finalPrice)
                .discount(discountValue)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        Mockito.when(discountProvider.getDiscountRate(quantity)).thenReturn(discountRate);
        Mockito.when(mapper.toProductPriceResponse(mockProduct, finalPrice, discountValue, quantity)).thenReturn(mockResponse);

        var request = new ProductPriceRequest(quantity);
        var response = productService.calculatePrice(productId, request);

        Assertions.assertEquals(mockResponse, response);
    }

    private Product createProduct(UUID id, String name, BigDecimal price) {

        var product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
