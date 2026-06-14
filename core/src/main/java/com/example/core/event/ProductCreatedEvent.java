package com.example.core.event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCreatedEvent {
    //contains same product details that are needed for creating product
    /*
    No args constructor need for deserialize purpose,
    when kafka publishes this event topic , it will be serialized into byte array....
    Consumer need to deserialize using this same event class --> consumer need noArgs Constructor
        -> for creating empty instance of this class
     */
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

}
