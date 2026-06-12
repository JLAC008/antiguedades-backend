package com.antiguedades.antique.dto;

import com.antiguedades.antique.AntiqueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record AntiqueRequest(
    String catalogId,
    @NotBlank String name,
    @NotNull AntiqueType type,
    @NotBlank String subcategory,
    @NotBlank String detail,
    String country,
    String region,
    String element,
    String title,
    String author,
    String editor,
    String imprenta,
    String edition,
    String signature,
    String theme,
    String century,
    String description,
    BigDecimal price,
    String yearEra,
    String condition,
    String material,
    String dimensions,
    String paperType,
    String paperFormat,
    Integer paperWeight,
    List<String> images
) {
}
