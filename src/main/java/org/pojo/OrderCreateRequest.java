package org.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderCreateRequest {

    private List<String> ingredients;

    public OrderCreateRequest (List<String> ingredients) {
        this.ingredients = ingredients;
    }

}