package com.hanbinlee.photoExhibition.service;


import com.hanbinlee.photoExhibition.repository.CityRepository;
import com.hanbinlee.photoExhibition.entity.City;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<String> getAllRegions() {
        return cityRepository.findAll().stream()
                .map(City::getName)
                .collect(Collectors.toList());
    }
}
