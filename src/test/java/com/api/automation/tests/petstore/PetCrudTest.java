package com.api.automation.tests.petstore;

import com.api.automation.petstore.client.PetApiClient;
import com.api.automation.petstore.factory.PetStoreTestDataFactory;
import com.api.automation.petstore.model.request.CreatePetRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@Epic("Enterprise API Automation")
@Feature("Petstore Pet API")
@Owner("Kumaresan Sitheswaran")
public class PetCrudTest {

    private final PetApiClient petApiClient = new PetApiClient();

    @Test
    @Story("Pet CRUD Workflow")
    @Description("Validates complete Petstore pet lifecycle: create, retrieve, update, find by status, delete, and verify 404 after deletion.")
    public void shouldCompletePetCrudWorkflow() {
        CreatePetRequest createRequest =
                PetStoreTestDataFactory.createAvailablePet();

        long petId = createRequest.getId();
        boolean petDeleted = false;

        try {
            Response createResponse =
                    createPet(createRequest);

            validateCreatedPetResponse(
                    createResponse,
                    petId,
                    createRequest
            );

            Response getResponse =
                    retrievePet(petId);

            validateRetrievedPetResponse(
                    getResponse,
                    petId,
                    createRequest
            );

            CreatePetRequest updateRequest =
                    PetStoreTestDataFactory.createSoldPetUpdate(createRequest);

            Response updateResponse =
                    updatePet(updateRequest);

            validateUpdatedPetResponse(
                    updateResponse,
                    petId,
                    updateRequest
            );

            Response updatedGetResponse =
                    retrievePet(petId);

            validateRetrievedUpdatedPetResponse(
                    updatedGetResponse,
                    petId,
                    updateRequest
            );

            Response findByStatusResponse =
                    findPetsByStatus("sold");

            validatePetPresentInStatusSearch(
                    findByStatusResponse,
                    petId
            );

            Response deleteResponse =
                    deletePet(petId);

            validatePetDeleted(deleteResponse);

            petDeleted = true;

            Response deletedPetResponse =
                    retrievePet(petId);

            validateDeletedPetNotFound(deletedPetResponse);

        } finally {
            if (!petDeleted) {
                cleanupPet(petId);
            }
        }
    }

    @Step("Create Petstore pet")
    private Response createPet(CreatePetRequest request) {
        return petApiClient.createPet(request);
    }

    @Step("Validate created Petstore pet response")
    private void validateCreatedPetResponse(
            Response response,
            long petId,
            CreatePetRequest request
    ) {
        response.then()
                .statusCode(200)
                .body("name", equalTo(request.getName()))
                .body("status", equalTo("available"));

        Assert.assertEquals(
                response.jsonPath().getLong("id"),
                petId,
                "Created pet ID did not match request ID"
        );
    }

    @Step("Retrieve Petstore pet with ID: {petId}")
    private Response retrievePet(long petId) {
        return petApiClient.getPet(petId);
    }

    @Step("Validate retrieved Petstore pet response")
    private void validateRetrievedPetResponse(
            Response response,
            long petId,
            CreatePetRequest request
    ) {
        response.then()
                .statusCode(200)
                .body("name", equalTo(request.getName()))
                .body("status", equalTo("available"));

        Assert.assertEquals(
                response.jsonPath().getLong("id"),
                petId,
                "Retrieved pet ID did not match created pet ID"
        );
    }

    @Step("Update Petstore pet")
    private Response updatePet(CreatePetRequest request) {
        return petApiClient.updatePet(request);
    }

    @Step("Validate updated Petstore pet response")
    private void validateUpdatedPetResponse(
            Response response,
            long petId,
            CreatePetRequest request
    ) {
        response.then()
                .statusCode(200)
                .body("name", equalTo(request.getName()))
                .body("status", equalTo("sold"));

        Assert.assertEquals(
                response.jsonPath().getLong("id"),
                petId,
                "Updated pet ID did not match created pet ID"
        );
    }

    @Step("Validate retrieved updated Petstore pet response")
    private void validateRetrievedUpdatedPetResponse(
            Response response,
            long petId,
            CreatePetRequest request
    ) {
        response.then()
                .statusCode(200)
                .body("name", equalTo(request.getName()))
                .body("status", equalTo("sold"));

        Assert.assertEquals(
                response.jsonPath().getLong("id"),
                petId,
                "Updated GET pet ID did not match created pet ID"
        );
    }

    @Step("Find Petstore pets by status: {status}")
    private Response findPetsByStatus(String status) {
        return petApiClient.findPetsByStatus(status);
    }

    @Step("Validate created pet is present in find-by-status response")
    private void validatePetPresentInStatusSearch(
            Response response,
            long petId
    ) {
        response.then()
                .statusCode(200);

        List<Number> petIds =
                response.jsonPath()
                        .getList("id");

        boolean petFound = petIds.stream()
                .anyMatch(id -> id != null && id.longValue() == petId);

        Assert.assertTrue(
                petFound,
                "Created pet was not returned by findByStatus"
        );
    }

    @Step("Delete Petstore pet with ID: {petId}")
    private Response deletePet(long petId) {
        return petApiClient.deletePet(petId);
    }

    @Step("Validate Petstore pet delete response")
    private void validatePetDeleted(Response response) {
        response.then()
                .statusCode(200);
    }

    @Step("Validate deleted Petstore pet returns 404")
    private void validateDeletedPetNotFound(Response response) {
        response.then()
                .statusCode(404);
    }

    @Step("Cleanup Petstore pet with ID: {petId}")
    private void cleanupPet(long petId) {
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