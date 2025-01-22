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
        String expectedRegion = "Seoul";
        String expectedSubRegion = "Yongsan";
        String expectedDescription = "Yongsan station";
        String expectedImageUrl = "http://example.com/seoul.jpg";
        City expectedCity = new City();

        photo.setId(expectedId);
        photo.setRegion(expectedRegion);
        photo.setSubRegion(expectedSubRegion);
        photo.setDescription(expectedDescription);
        photo.setImageURL(expectedImageUrl);
        photo.setCity(expectedCity);



        assertEquals(expectedId, photo.getId());
        assertEquals(expectedRegion, photo.getRegion());
        assertEquals(expectedSubRegion, photo.getSubRegion());
        assertEquals(expectedDescription, photo.getDescription());
        assertEquals(expectedImageUrl, photo.getImageURL());
        assertEquals(expectedCity, photo.getCity());
    }
}

