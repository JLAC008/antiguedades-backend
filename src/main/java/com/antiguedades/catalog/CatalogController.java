package com.antiguedades.catalog;

import com.antiguedades.catalog.dto.CatalogRequest;
import com.antiguedades.catalog.dto.CatalogResponse;
import com.antiguedades.security.JwtUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogs")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public ResponseEntity<List<CatalogResponse>> getAll() {
        return ResponseEntity.ok(catalogService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(catalogService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CatalogResponse> create(
            @Valid @RequestBody CatalogRequest request,
            @AuthenticationPrincipal JwtUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(catalogService.create(request, user != null ? user.username() : "anonymous"));
    }
}
