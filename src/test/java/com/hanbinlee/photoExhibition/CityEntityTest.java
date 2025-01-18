package com.hanbinlee.photoExhibition;

import com.hanbinlee.photoExhibition.entity.City;
import com.hanbinlee.photoExhibition.entity.Photo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CityEntityTest {

    @Test
    public void testGetterAndSetter() {
        City city = new City();
        Long expectedId = 1L;
        String expectedName = "Seoul";

        city.setId(expectedId);
        city.setName(expectedName);

        assertEquals(expectedId, city.getId());
        assertEquals(expectedName, city.getName());
    }

    @Test
    public void testPhotosGetterAndSetter() {
        City city = new City();
        List<Photo> expectedPhotos = new ArrayList<>();
        Photo photo1 = new Photo();
        photo1.setId(1L);
        photo1.setDescription("Photo 1");
        expectedPhotos.add(photo1);

        Photo photo2 = new Photo();
        photo2.setId(2L);
        photo2.setDescription("Photo 2");
        expectedPhotos.add(photo2);

        city.setPhotos(expectedPhotos);

        assertEquals(expectedPhotos, city.getPhotos());
        assertEquals(2, city.getPhotos().size());
        assertEquals("Photo 1", city.getPhotos().get(0).getDescription());
        assertEquals("Photo 2", city.getPhotos().get(1).getDescription());
    }
}
