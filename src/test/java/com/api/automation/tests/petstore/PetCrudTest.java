package com.api.automation.tests.petstore;

import com.api.automation.petstore.client.PetApiClient;
import com.api.automation.petstore.factory.PetStoreTestDataFactory;
import com.api.automation.petstore.model.request.CreatePetRequest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class PetCrudTest {

    private final PetApiClient petApiClient = new PetApiClient();

    @Test
    public void shouldCompletePetCrudWorkflow() {
        CreatePetRequest createRequest =
                PetStoreTestDataFactory.createAvailablePet();

        long petId = createRequest.getId();
        boolean petDeleted = false;

        try {
            // Create pet
            Response createResponse =
                    petApiClient.createPet(createRequest);

            createResponse.then()
                    .statusCode(200)
                    .body("name", equalTo(createRequest.getName()))
                    .body("status", equalTo("available"));

            Assert.assertEquals(
                    createResponse.jsonPath().getLong("id"),
                    petId,
                    "Created pet ID did not match request ID"
            );

            // Retrieve pet
            Response getResponse =
                    petApiClient.getPet(petId);

            getResponse.then()
                    .statusCode(200)
                    .body("name", equalTo(createRequest.getName()))
                    .body("status", equalTo("available"));

            Assert.assertEquals(
                    getResponse.jsonPath().getLong("id"),
                    petId,
                    "Retrieved pet ID did not match created pet ID"
            );

            // Update pet
            CreatePetRequest updateRequest =
                    PetStoreTestDataFactory.createSoldPetUpdate(createRequest);

            Response updateResponse =
                    petApiClient.updatePet(updateRequest);

            updateResponse.then()
                    .statusCode(200)
                    .body("name", equalTo(updateRequest.getName()))
                    .body("status", equalTo("sold"));

            Assert.assertEquals(
                    updateResponse.jsonPath().getLong("id"),
                    petId,
                    "Updated pet ID did not match created pet ID"
            );

            // Confirm update through GET
            Response updatedGetResponse =
                    petApiClient.getPet(petId);

            updatedGetResponse.then()
                    .statusCode(200)
                    .body("name", equalTo(updateRequest.getName()))
                    .body("status", equalTo("sold"));

            Assert.assertEquals(
                    updatedGetResponse.jsonPath().getLong("id"),
                    petId,
                    "Updated GET pet ID did not match created pet ID"
            );

            // Find pet by status
            Response findByStatusResponse =
                    petApiClient.findPetsByStatus("sold");

            findByStatusResponse.then()
                    .statusCode(200);

            List<Number> petIds =
                    findByStatusResponse.jsonPath()
                            .getList("id");

            boolean petFound = petIds.stream()
                    .anyMatch(id -> id.longValue() == petId);

            Assert.assertTrue(
                    petFound,
                    "Created pet was not returned by findByStatus"
            );

            // Delete pet
            Response deleteResponse =
                    petApiClient.deletePet(petId);

            deleteResponse.then()
                    .statusCode(200);

            petDeleted = true;

            // Verify deleted pet returns 404
            Response deletedPetResponse =
                    petApiClient.getPet(petId);

            deletedPetResponse.then()
                    .statusCode(404);

        } finally {
            if (!petDeleted) {
                performCleanup(petId);
            }
        }
    }

    private void performCleanup(long petId) {
        try {
            Response cleanupResponse =
                    petApiClient.deletePet(petId);

            int cleanupStatus = cleanupResponse.statusCode();

            if (cleanupStatus != 200 && cleanupStatus != 404) {
                System.err.printf(
                        "Pet cleanup returned status %d for pet ID %d%n",
                        cleanupStatus,
                        petId
                );
            }
        } catch (Exception exception) {
            System.err.printf(
                    "Pet cleanup failed for pet ID %d: %s%n",
                    petId,
                    exception.getMessage()
            );
        }
    }
}