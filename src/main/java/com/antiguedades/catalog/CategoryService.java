package com.antiguedades.catalog;

import com.antiguedades.catalog.dto.CategoryResponse;
import com.antiguedades.catalog.dto.CategoryResponse.DetailItem;
import com.antiguedades.catalog.dto.CategoryResponse.SubcategoryGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAll() {
        List<Category> rows = categoryRepository.findAllByOrderByAntiqueTypeAscSubcategoryKeyAscDetailKeyAsc();

        Map<String, Map<String, List<Category>>> grouped = new LinkedHashMap<>();
        for (Category row : rows) {
            grouped
                .computeIfAbsent(row.getAntiqueType(), k -> new LinkedHashMap<>())
                .computeIfAbsent(row.getSubcategoryKey(), k -> new ArrayList<>())
                .add(row);
        }

        List<CategoryResponse> result = new ArrayList<>();
        for (var typeEntry : grouped.entrySet()) {
            List<SubcategoryGroup> subs = new ArrayList<>();
            for (var subEntry : typeEntry.getValue().entrySet()) {
                List<Category> subRows = subEntry.getValue();
                String label = subRows.get(0).getSubcategoryLabel();
                List<DetailItem> details = subRows.stream()
                    .filter(r -> r.getDetailKey() != null)
                    .map(r -> new DetailItem(r.getDetailKey(), r.getDetailLabel(), r.getDetailDesc()))
                    .collect(Collectors.toList());
                subs.add(new SubcategoryGroup(subEntry.getKey(), label, details));
            }
            result.add(new CategoryResponse(typeEntry.getKey(), subs));
        }
        return result;
    }
}
