package com.antiguedades.antique;

import com.antiguedades.antique.dto.AntiqueRequest;
import com.antiguedades.antique.dto.AntiqueResponse;
import com.antiguedades.antique.dto.CountsResponse;
import com.antiguedades.security.JwtUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/antiques")
public class AntiqueController {
    private final AntiqueService antiqueService;

    public AntiqueController(AntiqueService antiqueService) {
        this.antiqueService = antiqueService;
    }

    @GetMapping
    public ResponseEntity<List<AntiqueResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) AntiqueType type,
            @RequestParam(required = false) String subcategory,
            @RequestParam(required = false) String detail,
            @RequestParam(required = false) String condition) {
        return ResponseEntity.ok(antiqueService.getAll(search, type, subcategory, detail, condition));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntiqueResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(antiqueService.getById(id));
    }

    @GetMapping("/counts")
    public ResponseEntity<CountsResponse> getCounts() {
        return ResponseEntity.ok(antiqueService.getCounts());
    }

    @PostMapping
    public ResponseEntity<AntiqueResponse> create(
            @Valid @RequestBody AntiqueRequest request,
            @AuthenticationPrincipal JwtUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(antiqueService.create(request, user != null ? user.username() : "anonymous"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AntiqueResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody AntiqueRequest request) {
        return ResponseEntity.ok(antiqueService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        antiqueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
