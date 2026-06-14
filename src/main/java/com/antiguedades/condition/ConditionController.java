package com.antiguedades.condition;

import com.antiguedades.condition.dto.ConditionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conditions")
public class ConditionController {
    private final ConditionService conditionService;

    public ConditionController(ConditionService conditionService) {
        this.conditionService = conditionService;
    }

    @GetMapping
    public ResponseEntity<List<ConditionResponse>> getAll() {
        return ResponseEntity.ok(conditionService.getAll());
    }
}
