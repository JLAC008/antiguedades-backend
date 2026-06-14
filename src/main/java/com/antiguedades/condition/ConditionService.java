package com.antiguedades.condition;

import com.antiguedades.condition.dto.ConditionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ConditionService {
    private final ConditionRepository conditionRepository;

    public ConditionService(ConditionRepository conditionRepository) {
        this.conditionRepository = conditionRepository;
    }

    public List<ConditionResponse> getAll() {
        return conditionRepository.findAllByOrderByIdAsc().stream()
            .map(c -> new ConditionResponse(c.getLabel()))
            .collect(Collectors.toList());
    }
}
