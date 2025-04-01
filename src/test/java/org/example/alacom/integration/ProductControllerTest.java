package org.example.alacom.integration;

import org.example.alacom.TestcontainersConfiguration;
import org.example.alacom.infrastructure.entity.Product;
import org.example.alacom.domain.dto.ProductResponse;
import org.example.alacom.domain.dto.ProductPriceRequest;
import org.example.alacom.domain.dto.ProductPriceResponse;
import org.example.alacom.infrastructure.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;

    private static Stream<Arguments> invalidProductPriceRequests() {
        return Stream.of(
                Arguments.of(new ProductPriceRequest(-1)),
                Arguments.of(new ProductPriceRequest(null))
        );
    }

    @AfterEach
    void cleanUp() {
        productRepository.deleteAll();
    }

    @Test
    public void getProduct() {

        var product = createProduct("fish", BigDecimal.valueOf(2.49));
        var expected = ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();

        var url = String.format("http://localhost:%d/api/v1/products/%s", port, product.getId());
        var response = restTemplate.getForEntity(url, ProductResponse.class);
        var actual = response.getBody();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void calculatePrice() {

        var product = createProduct("chair", BigDecimal.valueOf(74.99));
        var productPriceRequest = new ProductPriceRequest(2);

        var expected = ProductPriceResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(productPriceRequest.getQuantity())
                .unitPrice(product.getPrice())
                .finalPrice(BigDecimal.valueOf(147.73))
                .discount(BigDecimal.valueOf(2.25))
                .build();

        var url = String.format("http://localhost:%d/api/v1/products/%s/price", port, product.getId());
        var response = restTemplate.postForEntity(url, new HttpEntity<>(productPriceRequest), ProductPriceResponse.class);
        var actual = response.getBody();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("invalidProductPriceRequests")
    public void calculatePrice_InvalidProductPriceRequest(ProductPriceRequest productPriceRequest) {

        var url = String.format("http://localhost:%d/api/v1/products/%s/price", port, UUID.randomUUID());
        var response = restTemplate.postForEntity(url, new HttpEntity<>(productPriceRequest), Object.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private Product createProduct(String name, BigDecimal price) {

        var product = new Product();
        product.setName(name);
        product.setPrice(price);

        return productRepository.save(product);
    }
}
