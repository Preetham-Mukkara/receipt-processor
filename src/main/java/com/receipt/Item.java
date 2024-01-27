package com.receipt;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

@Introspected
@Serdeable
public class Item {
    @NotNull
    private String shortDescription;
    @NotNull
    private String price;

    public void setShortDescription(String shortDescription){
        this.shortDescription = shortDescription;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
