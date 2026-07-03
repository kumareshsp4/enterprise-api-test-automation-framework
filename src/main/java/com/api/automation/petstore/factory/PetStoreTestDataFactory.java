package com.api.automation.petstore.factory;

import com.api.automation.core.utils.RandomDataUtils;
import com.api.automation.petstore.model.common.Category;
import com.api.automation.petstore.model.common.Tag;
import com.api.automation.petstore.model.request.CreatePetRequest;

import java.util.List;

public final class PetStoreTestDataFactory {

    private PetStoreTestDataFactory() {
    }

    public static CreatePetRequest createAvailablePet() {
        int petId = RandomDataUtils.uniqueNumber();
        String suffix = RandomDataUtils.uniqueSuffix();

        Category category = Category.builder()
                .id(1)
                .name("Automation Dogs")
                .build();

        Tag tag = Tag.builder()
                .id(1)
                .name("api-automation")
                .build();

        return CreatePetRequest.builder()
                .id(petId)
                .category(category)
                .name("Automation Pet " + suffix)
                .photoUrls(List.of(
                        "https://example.com/pets/" + petId + ".jpg"
                ))
                .tags(List.of(tag))
                .status("available")
                .build();
    }

    public static CreatePetRequest createSoldPetUpdate(
            CreatePetRequest originalPet
    ) {
        return originalPet.toBuilder()
                .name("Updated " + originalPet.getName())
                .status("sold")
                .build();
    }
}