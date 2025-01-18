package com.hanbinlee.photoExhibition.repository;

import java.util.Optional;
import com.hanbinlee.photoExhibition.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional <City> findByName(String name);


}

