package com.api.automation.petstore.model.request;

import com.api.automation.petstore.model.common.Category;
import com.api.automation.petstore.model.common.Tag;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreatePetRequest {
    public long id;
    public Category category;
    public String name;
    public List<String> photoUrls;
    public List<Tag> tags;
    public String status;
}



