package org.example.alacom.integration;

import org.example.alacom.TestcontainersConfiguration;
import org.example.alacom.domain.service.DiscountProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class DiscountProviderTest {

    @Autowired
    private DiscountProvider discountProvider;

    private static Stream<Arguments> discountThresholds() {
        return Stream.of(
                Arguments.of(0, BigDecimal.valueOf(0.015)),
                Arguments.of(9, BigDecimal.valueOf(0.015)),
                Arguments.of(10, BigDecimal.valueOf(0.05)),
                Arguments.of(20, BigDecimal.valueOf(0.1)),
                Arguments.of(50, BigDecimal.valueOf(0.15))
        );
    }

    @ParameterizedTest
    @MethodSource("discountThresholds")
    public void getDiscountRate(Integer unitCount, BigDecimal expectedDiscount) {

        var expected = expectedDiscount.setScale(4, RoundingMode.HALF_UP);
        var actual = discountProvider.getDiscountRate(unitCount);

        Assertions.assertEquals(expected, actual);
    }
}
