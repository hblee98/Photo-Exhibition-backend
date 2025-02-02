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
        String expectedWebContentLink = "http://example.com/seoul.jpg";
        int expectedWidth = 400;
        int expectedHeight = 600;
        City expectedCity = new City();

        photo.setId(expectedId);
        photo.setRegion(expectedRegion);
        photo.setSubRegion(expectedSubRegion);
        photo.setDescription(expectedDescription);
        photo.setWebContentLink(expectedWebContentLink);
        photo.setCity(expectedCity);
        photo.setWidth(expectedWidth);

        assertEquals(expectedId, photo.getId());
        assertEquals(expectedRegion, photo.getRegion());
        assertEquals(expectedSubRegion, photo.getSubRegion());
        assertEquals(expectedDescription, photo.getDescription());
        assertEquals(expectedWebContentLink, photo.getWebContentLink());
        assertEquals(expectedCity, photo.getCity());
        assertEquals(expectedWidth, photo.getWidth());
        assertEquals(expectedHeight, photo.getHeight());
    }
}

