package com.metro.repository;

import com.metro.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, String> {
}
