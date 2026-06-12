package com.antiguedades.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public record CatalogRequest(@NotBlank String name, String description, String coverImage) {
}
