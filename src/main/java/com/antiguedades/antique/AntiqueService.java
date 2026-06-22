package com.antiguedades.antique;

import com.antiguedades.antique.dto.AntiqueRequest;
import com.antiguedades.antique.dto.AntiqueResponse;
import com.antiguedades.antique.dto.CountsResponse;
import com.antiguedades.catalog.Catalog;
import com.antiguedades.catalog.CatalogRepository;
import com.antiguedades.catalog.dto.CatalogResponse;
import com.antiguedades.exception.BusinessException;
import com.antiguedades.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AntiqueService {
    private final AntiqueRepository antiqueRepository;
    private final CatalogRepository catalogRepository;

    public AntiqueService(AntiqueRepository antiqueRepository, CatalogRepository catalogRepository) {
        this.antiqueRepository = antiqueRepository;
        this.catalogRepository = catalogRepository;
    }

    public List<AntiqueResponse> getAll(String search, AntiqueType type, String subcategory,
                                        String detail, String condition) {
        if (search != null || type != null || subcategory != null || detail != null || condition != null) {
            return antiqueRepository.search(search, type, subcategory, detail, condition).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        }
        return antiqueRepository.findAllOrderByCreatedAtDesc().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<AntiqueResponse> getByCatalog(UUID catalogId) {
        return antiqueRepository.findByCatalogIdOrderByCreatedAtDesc(catalogId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public AntiqueResponse getById(UUID id) {
        Antique antique = antiqueRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pieza no encontrada"));
        return toResponse(antique);
    }

    @Transactional
    public AntiqueResponse create(AntiqueRequest request, String createdBy) {
        validateUniqueName(request.name(), request.allowDuplicateName(), null);
        Antique antique = new Antique();
        applyRequest(antique, request);
        if (request.catalogId() != null && !request.catalogId().isBlank()) {
            antique.setCatalogId(UUID.fromString(request.catalogId()));
        }
        antique.setCreatedBy(createdBy);
        antique = antiqueRepository.save(antique);
        return toResponse(antique);
    }

    @Transactional
    public AntiqueResponse update(UUID id, AntiqueRequest request) {
        Antique antique = antiqueRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pieza no encontrada"));
        validateUniqueName(request.name(), request.allowDuplicateName(), id);
        applyRequest(antique, request);
        if (request.catalogId() != null && !request.catalogId().isBlank()) {
            antique.setCatalogId(UUID.fromString(request.catalogId()));
        } else {
            antique.setCatalogId(null);
        }
        antique = antiqueRepository.save(antique);
        return toResponse(antique);
    }

    @Transactional
    public void delete(UUID id) {
        if (!antiqueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pieza no encontrada");
        }
        antiqueRepository.deleteById(id);
    }

    public CountsResponse getCounts() {
        long antiguedad = antiqueRepository.countByType(AntiqueType.antiguedad);
        long papeleria = antiqueRepository.countByType(AntiqueType.papeleria);
        return new CountsResponse(antiguedad, papeleria);
    }

    private void applyRequest(Antique antique, AntiqueRequest request) {
        antique.setName(request.name().trim());
        antique.setAllowDuplicateName(Boolean.TRUE.equals(request.allowDuplicateName()));
        antique.setType(request.type());
        antique.setSubcategory(request.subcategory());
        antique.setDetail(request.detail() != null ? request.detail().trim() : "");
        antique.setCountry(request.country());
        antique.setRegion(request.region());
        antique.setElement(request.element());
        antique.setTitle(request.title());
        antique.setAuthor(request.author());
        antique.setEditor(request.editor());
        antique.setImprenta(request.imprenta());
        antique.setEdition(request.edition());
        antique.setSignature(request.signature());
        antique.setTheme(request.theme());
        antique.setCentury(request.century());
        antique.setDescription(request.description());
        antique.setPrice(request.price() != null ? request.price() : BigDecimal.ZERO);
        antique.setYearEra(request.yearEra() != null ? request.yearEra().trim() : "");
        antique.setCondition(request.condition());
        antique.setMaterial(request.material());
        antique.setDimensions(request.dimensions());
        antique.setPaperType(request.paperType());
        antique.setPaperFormat(request.paperFormat());
        antique.setPaperWeight(request.paperWeight());
        antique.setImages(request.images() != null ? request.images() : List.of());
    }

    private void validateUniqueName(String name, Boolean allowDuplicateName, UUID currentId) {
        if (Boolean.TRUE.equals(allowDuplicateName)) {
            return;
        }
        String normalizedName = name.trim();
        boolean duplicate = currentId == null
            ? antiqueRepository.existsByNameIgnoreCase(normalizedName)
            : antiqueRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, currentId);
        if (duplicate) {
            throw new BusinessException("Ya existe una pieza con el nombre '" + normalizedName + "'.");
        }
    }

    private AntiqueResponse toResponse(Antique antique) {
        CatalogResponse catalogResponse = antique.getCatalogId() != null
            ? catalogRepository.findById(antique.getCatalogId())
                .map(c -> new CatalogResponse(c.getId(), c.getName(), c.getDescription(),
                    c.getCoverImage(), c.getCreatedBy(), c.getCreatedAt(), 0))
                .orElse(null)
            : null;
        return new AntiqueResponse(
            antique.getId(), antique.getCatalogId(), antique.getName(), antique.isAllowDuplicateName(), antique.getType(),
            antique.getSubcategory(), antique.getDetail(), antique.getCountry(), antique.getRegion(), antique.getElement(),
            antique.getTitle(), antique.getAuthor(), antique.getEditor(), antique.getImprenta(),
            antique.getEdition(), antique.getSignature(), antique.getTheme(), antique.getCentury(),
            antique.getDescription(), antique.getPrice(), antique.getYearEra(), antique.getCondition(),
            antique.getMaterial(), antique.getDimensions(),
            antique.getPaperType(), antique.getPaperFormat(), antique.getPaperWeight(),
            antique.getImages(), antique.getCreatedBy(), antique.getCreatedAt(),
            catalogResponse
        );
    }
}
