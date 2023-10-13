package com.api.automation.petstore.endpoint;

public final class PetStoreEndpoints {

    public static final String PETS_BY_STATUS = "/pet/findByStatus";
    public static final String PET_BY_ID = "/pet/{petId}";
    public static final String PETS = "/pet";
    public static final String ORDERS = "/store/order";
    public static final String ORDER_BY_ID = "/store/order/{orderId}";

    private PetStoreEndpoints() {
    }
}
