package com.api.automation.petstore.client;

import com.api.automation.core.client.BaseApiClient;
import com.api.automation.core.specification.RequestSpecFactory;
import com.api.automation.petstore.endpoint.PetStoreEndpoints;
import com.api.automation.petstore.model.request.CreatePetRequest;
import io.restassured.response.Response;

public final class PetApiClient extends BaseApiClient {

    public Response createPet(CreatePetRequest request) {
        return post(
                RequestSpecFactory.petStoreRequest(),
                PetStoreEndpoints.PETS,
                request
        );
    }

    public Response getPet(long petId) {
        return get(
                RequestSpecFactory.petStoreRequest()
                        .pathParam("petId", petId),
                PetStoreEndpoints.PET_BY_ID
        );
    }

    public Response updatePet(CreatePetRequest request) {
        return put(
                RequestSpecFactory.petStoreRequest(),
                PetStoreEndpoints.PETS,
                request
        );
    }

    public Response findPetsByStatus(String status) {
        return get(
                RequestSpecFactory.petStoreRequest()
                        .queryParam("status", status),
                PetStoreEndpoints.PETS_BY_STATUS
        );
    }

    public Response deletePet(long petId) {
        return delete(
                RequestSpecFactory.petStoreRequest()
                        .pathParam("petId", petId),
                PetStoreEndpoints.PET_BY_ID
        );
    }
}