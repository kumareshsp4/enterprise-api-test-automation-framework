package com.api.automation.tests;

import com.api.automation.core.config.ConfigManager;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ConfigManagerTest {

    @Test
    public void shouldLoadServiceBaseUrls() {
        String goRestUrl =
                ConfigManager.get("gorest.base.url");

        String petStoreUrl =
                ConfigManager.get("petstore.base.url");

        assertNotNull(goRestUrl);
        assertNotNull(petStoreUrl);

        assertEquals(
                goRestUrl,
                "https://gorest.co.in/public/v2"
        );

        assertEquals(
                petStoreUrl,
                "https://petstore3.swagger.io/api/v3"
        );
    }
}