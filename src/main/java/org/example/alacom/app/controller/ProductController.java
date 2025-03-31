package org.example.alacom.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.alacom.domain.dto.ProductResponse;
import org.example.alacom.domain.dto.ProductPriceRequest;
import org.example.alacom.domain.dto.ProductPriceResponse;
import org.example.alacom.domain.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ProductResponse getProduct(
            @PathVariable UUID id
    ) {
        return productService.getProduct(id);
    }

    @PostMapping("{id}/price")
    public ProductPriceResponse calculatePrice(
            @PathVariable UUID id,
            @Valid @RequestBody ProductPriceRequest productPriceRequest
    ) {
        return productService.calculatePrice(id, productPriceRequest);
    }
}
