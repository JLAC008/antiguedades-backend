package com.antiguedades.catalog.dto;

import java.util.List;

public record CategoryResponse(
    String type,
    List<SubcategoryGroup> subcategories
) {
    public record DetailItem(String key, String label, String desc) {}
    public record SubcategoryGroup(String key, String label, List<DetailItem> details) {}
}
