package com.antiguedades.catalog.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CatalogResponse(
    UUID id, String name, String description, String coverImage,
    String createdBy, LocalDateTime createdAt, long antiquesCount
) {
}
