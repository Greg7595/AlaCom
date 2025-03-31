package org.example.alacom.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductPriceResponse {

    private UUID id;
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal finalPrice;
    private BigDecimal discount;
}
