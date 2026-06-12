package com.antiguedades.catalog;

import com.antiguedades.catalog.dto.CatalogRequest;
import com.antiguedades.catalog.dto.CatalogResponse;
import com.antiguedades.antique.AntiqueRepository;
import com.antiguedades.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CatalogService {
    private final CatalogRepository catalogRepository;
    private final AntiqueRepository antiqueRepository;

    public CatalogService(CatalogRepository catalogRepository, AntiqueRepository antiqueRepository) {
        this.catalogRepository = catalogRepository;
        this.antiqueRepository = antiqueRepository;
    }

    public List<CatalogResponse> getAll() {
        return catalogRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public CatalogResponse getById(UUID id) {
        Catalog catalog = catalogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Catálogo no encontrado"));
        return toResponse(catalog);
    }

    @Transactional
    public CatalogResponse create(CatalogRequest request, String createdBy) {
        Catalog catalog = new Catalog();
        catalog.setName(request.name());
        catalog.setDescription(request.description());
        catalog.setCoverImage(request.coverImage());
        catalog.setCreatedBy(createdBy);
        catalog = catalogRepository.save(catalog);
        return toResponse(catalog);
    }

    private CatalogResponse toResponse(Catalog catalog) {
        long count = antiqueRepository.countByCatalogId(catalog.getId());
        return new CatalogResponse(
            catalog.getId(), catalog.getName(), catalog.getDescription(),
            catalog.getCoverImage(), catalog.getCreatedBy(), catalog.getCreatedAt(), count
        );
    }
}
