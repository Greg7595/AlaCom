package org.example.alacom.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.alacom.app.config.DiscountConfig;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class DiscountProvider {

    private final DiscountConfig discountConfig;

    public BigDecimal getDiscountRate(Integer unitCount) {

        var fixedDiscount = convertToPercentage(discountConfig.getFixedValue());
        var thresholdDiscount = discountConfig.getThresholds()
                .stream()
                .filter(threshold -> threshold.getRequiredQuantity() <= unitCount)
                .max(Comparator.comparingInt(DiscountConfig.DiscountThreshold::getRequiredQuantity))
                .map(threshold -> convertToPercentage(threshold.getValue()))
                .orElse(BigDecimal.ZERO);

        return fixedDiscount.max(thresholdDiscount);
    }

    private BigDecimal convertToPercentage(String value) {
        return new BigDecimal(value.replace("%", ""))
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }
}
