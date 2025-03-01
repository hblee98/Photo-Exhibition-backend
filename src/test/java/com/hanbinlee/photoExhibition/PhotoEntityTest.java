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
        String expectedRegion = "Brisbane";
        String expectedSubRegion = "BrisbaneCBD";
        String expectedDescription = "city-hall";
        String expectedOriginFilePath = "src/main/resources/sample image/Brisbane/BrisbaneCBD city-hall";
        String expectedThumbnailFilePath = "src/main/resources/sample image/Brisbane/mobile/BrisbaneCBD city-hall";

        int expectedWidth = 2000;
        int expectedHeight = 1333;
        City expectedCity = new City();

        photo.setId(expectedId);
        photo.setRegion(expectedRegion);
        photo.setSubRegion(expectedSubRegion);
        photo.setDescription(expectedDescription);
        photo.setOriginFilePath(expectedOriginFilePath);
        photo.setThumbnailFilePath(expectedThumbnailFilePath);
        photo.setCity(expectedCity);
        photo.setWidth(expectedWidth);
        photo.setHeight(expectedHeight);

        assertEquals(expectedId, photo.getId());
        assertEquals(expectedRegion, photo.getRegion());
        assertEquals(expectedSubRegion, photo.getSubRegion());
        assertEquals(expectedDescription, photo.getDescription());
        assertEquals(expectedOriginFilePath, photo.getOriginFilePath());
        assertEquals(expectedThumbnailFilePath, photo.getThumbnailFilePath());
        assertEquals(expectedCity, photo.getCity());
        assertEquals(expectedWidth, photo.getWidth());
        assertEquals(expectedHeight, photo.getHeight());
    }
}

