package com.hanbinlee.photoExhibition;

import com.hanbinlee.photoExhibition.entity.City;
import com.hanbinlee.photoExhibition.entity.Photo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhotoEntityTest {

    @Test
    public void testGetterAndSetter() {

        Photo photo = new Photo();
        Long expectedId = 1L;
        String expectedSubRegion = "Yongsan";
        String expectedDescription = "Yongsan station";
        String expectedImageUrl = "http://example.com/seoul.jpg";
        City expectedCity = new City();

        photo.setId(expectedId);
        photo.setSubRegion(expectedSubRegion);
        photo.setDescription(expectedDescription);
        photo.setImageUrl(expectedImageUrl);
        photo.setCity(expectedCity);



        assertEquals(expectedId, photo.getId());
        assertEquals(expectedSubRegion, photo.getSubRegion());
        assertEquals(expectedDescription, photo.getDescription());
        assertEquals(expectedImageUrl, photo.getImageUrl());
        assertEquals(expectedCity, photo.getCity());
    }
}

