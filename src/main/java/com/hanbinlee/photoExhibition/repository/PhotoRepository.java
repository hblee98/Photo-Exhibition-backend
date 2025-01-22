package com.hanbinlee.photoExhibition.repository;

import com.hanbinlee.photoExhibition.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    boolean existsByImageURL(String imageURL);
    List<Photo> findByCityName(String cityName);

}
