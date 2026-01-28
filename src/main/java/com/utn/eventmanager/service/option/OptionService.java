package com.utn.eventmanager.service.option;

import com.utn.eventmanager.dto.option.OptionCreateRequest;
import com.utn.eventmanager.dto.option.OptionResponse;
import com.utn.eventmanager.dto.option.OptionUpdateRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OptionService {
    OptionResponse create(OptionCreateRequest request, Authentication authentication);

    OptionResponse update(Long id, OptionUpdateRequest request, Authentication authentication);

    List<OptionResponse> findAll();

    List<OptionResponse> findAvailable();
    void delete(Authentication authentication, Long id);
    List<OptionResponse> findByElement(Long elementId);
}