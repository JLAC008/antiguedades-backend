package com.antiguedades.antique.dto;

import com.antiguedades.antique.AntiqueType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record AntiqueRequest(
    String catalogId,
    @NotBlank @Size(max = 200) String name,
    Boolean allowDuplicateName,
    @NotNull AntiqueType type,
    @NotBlank @Size(max = 200) String subcategory,
    @NotBlank @Size(max = 200) String detail,
    @Size(max = 200) String country,
    @Size(max = 200) String region,
    @Size(max = 200) String element,
    @Size(max = 200) String title,
    @Size(max = 200) String author,
    @Size(max = 200) String editor,
    @Size(max = 200) String imprenta,
    @Size(max = 200) String edition,
    @Size(max = 200) String signature,
    @Size(max = 200) String theme,
    @Size(max = 100) String century,
    @Size(max = 2000) String description,
    @NotNull @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0") BigDecimal price,
    @Size(max = 100) String yearEra,
    @NotBlank @Size(max = 50) String condition,
    @Size(max = 200) String material,
    @Size(max = 200) String dimensions,
    @Size(max = 100) String paperType,
    @Size(max = 100) String paperFormat,
    Integer paperWeight,
    @Size(max = 5, message = "Máximo 5 imágenes por pieza") List<String> images
) {
}
