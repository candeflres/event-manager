package com.utn.eventmanager.service.option;

import com.utn.eventmanager.dto.option.OptionCreateRequest;
import com.utn.eventmanager.dto.option.OptionResponse;
import com.utn.eventmanager.dto.option.OptionUpdateRequest;

import java.util.List;

public interface OptionService {
    OptionResponse create(OptionCreateRequest request);

    OptionResponse update(Long id, OptionUpdateRequest request);

    List<OptionResponse> findAll();

    List<OptionResponse> findAvailable();

    List<OptionResponse> findByElement(Long elementId);
}