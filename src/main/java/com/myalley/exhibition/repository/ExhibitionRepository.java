package com.myalley.exhibition.repository;

import com.myalley.exhibition.domain.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionRepository extends JpaRepository<Exhibition,Long> {

}
