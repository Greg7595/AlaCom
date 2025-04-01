package org.example.alacom.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductPriceRequest {

    @NotNull
    @PositiveOrZero
    private Integer quantity;
}
