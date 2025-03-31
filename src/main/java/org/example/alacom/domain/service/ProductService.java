package org.example.alacom.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.alacom.domain.dto.ProductResponse;
import org.example.alacom.domain.dto.ProductPriceRequest;
import org.example.alacom.domain.dto.ProductPriceResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    public ProductResponse getProduct(UUID id) {
        return null;
    }

    public ProductPriceResponse calculatePrice(UUID id, ProductPriceRequest productPriceRequest) {
        return null;
    }
}
