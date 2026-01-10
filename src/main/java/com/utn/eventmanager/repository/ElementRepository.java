package com.utn.eventmanager.repository;

import com.utn.eventmanager.model.Element;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElementRepository extends JpaRepository<Element, Long> {

    List<Element> findByAvailableTrue();

    boolean existsByNameIgnoreCase(String name);
}
