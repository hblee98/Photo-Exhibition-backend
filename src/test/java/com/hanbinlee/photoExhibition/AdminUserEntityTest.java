package com.hanbinlee.photoExhibition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.hanbinlee.photoExhibition.entity.AdminUser;
public class AdminUserEntityTest {
    @Test
    public void testAdminUserGettersAndSetters() {
        // Create a new instance of the AdminUser entity
        AdminUser adminUser = new AdminUser();

        // Set values using setters
        Long expectedId = 1L;
        String expectedUsername = "adminUser";
        String expectedPassword = "secretPassword";

        adminUser.setId(expectedId);
        adminUser.setUsername(expectedUsername);
        adminUser.setPassword(expectedPassword);

        // Verify that the getters return the correct values
        assertEquals(expectedId, adminUser.getId(), "ID should be set and retrieved correctly");
        assertEquals(expectedUsername, adminUser.getUsername(), "Username should be set and retrieved correctly");
        assertEquals(expectedPassword, adminUser.getPassword(), "Password should be set and retrieved correctly");
    }
}
