package com.antiguedades.antique.dto;

import com.antiguedades.antique.AntiqueType;
import com.antiguedades.catalog.dto.CatalogResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AntiqueResponse(
    UUID id, UUID catalogId, String name, boolean allowDuplicateName, AntiqueType type,
    String subcategory, String detail, String country, String region, String element,
    String title, String author, String editor, String imprenta, String edition,
    String signature, String theme, String century, String description,
    BigDecimal price, String yearEra, String condition, String material, String dimensions,
    String paperType, String paperFormat, Integer paperWeight,
    List<String> images, String createdBy, LocalDateTime createdAt,
    CatalogResponse catalog
) {
}
