package com.api.automation.petstore.model.common;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    public long id;
    public String name;
}
